package com.compare.service;

import com.compare.config.RankingConfig;
import com.compare.domain.QuoteResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service("defaultRankingService")
public class DefaultRankingService implements RankingService {

    private static final Logger log = LoggerFactory.getLogger(DefaultRankingService.class);

    private final RankingConfig rankingConfig;

    public DefaultRankingService(RankingConfig rankingConfig) {
        this.rankingConfig = rankingConfig;
    }

    @Override
    public List<QuoteResult> rank(List<QuoteResult> results) {
        if (results.isEmpty()) return results;

        double maxPrice = results.stream().mapToDouble(QuoteResult::getAnnualPrice).max().orElse(1);
        double minPrice = results.stream().mapToDouble(QuoteResult::getAnnualPrice).min().orElse(0);
        double priceRange = maxPrice - minPrice;
        if (priceRange == 0) priceRange = 1;

        double maxRating = results.stream().mapToDouble(QuoteResult::getRating).max().orElse(1);
        double minRating = results.stream().mapToDouble(QuoteResult::getRating).min().orElse(0);
        double ratingRange = maxRating - minRating;
        if (ratingRange == 0) ratingRange = 1;

        for (QuoteResult r : results) {
            double normalizedPrice = 1.0 - (r.getAnnualPrice() - minPrice) / priceRange;
            double normalizedRating = (r.getRating() - minRating) / ratingRange;
            double featureScore = calculateFeatureScore(r.getFeatures());

            double score = rankingConfig.getPriceWeight() * normalizedPrice
                    + rankingConfig.getRatingWeight() * normalizedRating
                    + rankingConfig.getFeatureWeight() * featureScore;

            r.setScore(Math.round(score * 1000.0) / 1000.0);
        }

        results.sort(Comparator.comparingDouble(QuoteResult::getScore).reversed());

        // Mark best price and recommended
        QuoteResult cheapest = results.stream()
                .min(Comparator.comparingDouble(QuoteResult::getAnnualPrice))
                .orElse(null);
        if (cheapest != null) cheapest.setIsBestPrice(true);

        if (!results.isEmpty()) results.get(0).setIsRecommended(true);

        log.info("Ranked {} results. Best price: {}, Recommended: {}",
                results.size(),
                cheapest != null ? cheapest.getProviderName() : "none",
                results.get(0).getProviderName());

        return results;
    }

    private double calculateFeatureScore(String features) {
        if (features == null || features.isBlank()) return 0.0;
        String[] featureList = features.split(",");
        return Math.min(featureList.length / 8.0, 1.0);
    }
}
