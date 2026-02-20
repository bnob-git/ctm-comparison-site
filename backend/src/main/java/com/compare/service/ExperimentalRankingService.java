package com.compare.service;

import com.compare.config.RankingConfig;
import com.compare.domain.QuoteResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service("experimentalRankingService")
public class ExperimentalRankingService implements RankingService {

    private static final Logger log = LoggerFactory.getLogger(ExperimentalRankingService.class);

    private final RankingConfig rankingConfig;

    public ExperimentalRankingService(RankingConfig rankingConfig) {
        this.rankingConfig = rankingConfig;
    }

    @Override
    public List<QuoteResult> rank(List<QuoteResult> results) {
        if (results.isEmpty()) return results;

        log.info("Using EXPERIMENTAL ranking algorithm");

        double maxPrice = results.stream().mapToDouble(QuoteResult::getAnnualPrice).max().orElse(1);
        double minPrice = results.stream().mapToDouble(QuoteResult::getAnnualPrice).min().orElse(0);
        double priceRange = maxPrice - minPrice;
        if (priceRange == 0) priceRange = 1;

        double maxRating = results.stream().mapToDouble(QuoteResult::getRating).max().orElse(1);
        double minRating = results.stream().mapToDouble(QuoteResult::getRating).min().orElse(0);
        double ratingRange = maxRating - minRating;
        if (ratingRange == 0) ratingRange = 1;

        // Experimental: boost rating weight, add value-for-money metric
        for (QuoteResult r : results) {
            double normalizedPrice = 1.0 - (r.getAnnualPrice() - minPrice) / priceRange;
            double normalizedRating = (r.getRating() - minRating) / ratingRange;
            double featureScore = calculateFeatureScore(r.getFeatures());

            // Value-for-money: rating per pound
            double valueScore = r.getRating() / (r.getAnnualPrice() / 1000.0);
            double normalizedValue = Math.min(valueScore / 10.0, 1.0);

            double score = 0.3 * normalizedPrice
                    + 0.35 * normalizedRating
                    + 0.15 * featureScore
                    + 0.2 * normalizedValue;

            r.setScore(Math.round(score * 1000.0) / 1000.0);
        }

        results.sort(Comparator.comparingDouble(QuoteResult::getScore).reversed());

        QuoteResult cheapest = results.stream()
                .min(Comparator.comparingDouble(QuoteResult::getAnnualPrice))
                .orElse(null);
        if (cheapest != null) cheapest.setIsBestPrice(true);

        if (!results.isEmpty()) results.get(0).setIsRecommended(true);

        return results;
    }

    private double calculateFeatureScore(String features) {
        if (features == null || features.isBlank()) return 0.0;
        String[] featureList = features.split(",");
        return Math.min(featureList.length / 8.0, 1.0);
    }
}
