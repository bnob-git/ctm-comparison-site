package com.compare.service;

import com.compare.config.RankingConfig;
import com.compare.domain.QuoteResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

        double maxPrice = results.stream().mapToDouble(QuoteResult::annualPrice).max().orElse(1);
        double minPrice = results.stream().mapToDouble(QuoteResult::annualPrice).min().orElse(0);
        double priceRange = maxPrice - minPrice;
        if (priceRange == 0) priceRange = 1;

        double maxRating = results.stream().mapToDouble(QuoteResult::rating).max().orElse(1);
        double minRating = results.stream().mapToDouble(QuoteResult::rating).min().orElse(0);
        double ratingRange = maxRating - minRating;
        if (ratingRange == 0) ratingRange = 1;

        List<QuoteResult> scored = new ArrayList<>();
        for (QuoteResult r : results) {
            double normalizedPrice = 1.0 - (r.annualPrice() - minPrice) / priceRange;
            double normalizedRating = (r.rating() - minRating) / ratingRange;
            double featureScore = calculateFeatureScore(r.features());

            double valueScore = r.rating() / (r.annualPrice() / 1000.0);
            double normalizedValue = Math.min(valueScore / 10.0, 1.0);

            double score = 0.3 * normalizedPrice
                    + 0.35 * normalizedRating
                    + 0.15 * featureScore
                    + 0.2 * normalizedValue;

            scored.add(new QuoteResult(
                    r.providerId(), r.providerName(), r.monthlyPrice(), r.annualPrice(),
                    r.rating(), r.features(), r.exclusions(), r.coverLevel(),
                    Math.round(score * 1000.0) / 1000.0, false, false
            ));
        }

        scored.sort(Comparator.comparingDouble(QuoteResult::score).reversed());

        QuoteResult cheapest = scored.stream()
                .min(Comparator.comparingDouble(QuoteResult::annualPrice))
                .orElse(null);

        List<QuoteResult> finalResults = new ArrayList<>();
        for (int i = 0; i < scored.size(); i++) {
            QuoteResult r = scored.get(i);
            boolean isBest = cheapest != null && r.providerId().equals(cheapest.providerId());
            boolean isRec = i == 0;
            finalResults.add(new QuoteResult(
                    r.providerId(), r.providerName(), r.monthlyPrice(), r.annualPrice(),
                    r.rating(), r.features(), r.exclusions(), r.coverLevel(),
                    r.score(), isBest, isRec
            ));
        }

        return finalResults;
    }

    private double calculateFeatureScore(String features) {
        if (features == null || features.isBlank()) return 0.0;
        String[] featureList = features.split(",");
        return Math.min(featureList.length / 8.0, 1.0);
    }
}
