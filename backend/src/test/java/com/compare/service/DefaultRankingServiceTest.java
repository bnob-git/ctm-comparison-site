package com.compare.service;

import com.compare.config.RankingConfig;
import com.compare.domain.QuoteResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DefaultRankingServiceTest {

    private DefaultRankingService rankingService;

    @BeforeEach
    void setUp() {
        RankingConfig config = new RankingConfig();
        config.setPriceWeight(0.5);
        config.setRatingWeight(0.3);
        config.setFeatureWeight(0.2);
        rankingService = new DefaultRankingService(config);
    }

    @Test
    void shouldReturnEmptyListForEmptyInput() {
        List<QuoteResult> results = rankingService.rank(new ArrayList<>());
        assertTrue(results.isEmpty());
    }

    @Test
    void shouldMarkCheapestAsBestPrice() {
        List<QuoteResult> results = createSampleResults();
        List<QuoteResult> ranked = rankingService.rank(results);

        long bestPriceCount = ranked.stream().filter(QuoteResult::isBestPrice).count();
        assertEquals(1, bestPriceCount, "Exactly one result should be marked as best price");

        QuoteResult bestPrice = ranked.stream().filter(QuoteResult::isBestPrice).findFirst().orElseThrow();
        double minPrice = ranked.stream().mapToDouble(QuoteResult::annualPrice).min().orElse(0);
        assertEquals(minPrice, bestPrice.annualPrice(), "Best price should be the cheapest");
    }

    @Test
    void shouldMarkFirstRankedAsRecommended() {
        List<QuoteResult> results = createSampleResults();
        List<QuoteResult> ranked = rankingService.rank(results);

        assertTrue(ranked.get(0).isRecommended(), "First result should be recommended");
        long recommendedCount = ranked.stream().filter(QuoteResult::isRecommended).count();
        assertEquals(1, recommendedCount, "Exactly one result should be recommended");
    }

    @Test
    void shouldAssignScores() {
        List<QuoteResult> results = createSampleResults();
        List<QuoteResult> ranked = rankingService.rank(results);

        for (QuoteResult r : ranked) {
            assertTrue(r.score() >= 0 && r.score() <= 1.0,
                    "Score should be between 0 and 1: " + r.score());
        }
    }

    @Test
    void shouldSortByScoreDescending() {
        List<QuoteResult> results = createSampleResults();
        List<QuoteResult> ranked = rankingService.rank(results);

        for (int i = 0; i < ranked.size() - 1; i++) {
            assertTrue(ranked.get(i).score() >= ranked.get(i + 1).score(),
                    "Results should be sorted by score descending");
        }
    }

    @Test
    void shouldHandleSingleResult() {
        QuoteResult result = new QuoteResult(
                1L, "Solo", 0, 500, 4.0,
                "Feature1,Feature2", null, null, 0, false, false
        );

        List<QuoteResult> ranked = rankingService.rank(new ArrayList<>(List.of(result)));

        assertEquals(1, ranked.size());
        assertTrue(ranked.get(0).isBestPrice());
        assertTrue(ranked.get(0).isRecommended());
    }

    private List<QuoteResult> createSampleResults() {
        List<QuoteResult> results = new ArrayList<>();
        results.add(new QuoteResult(1L, "CheapCo", 35, 400, 3.5, "Feature1,Feature2", null, null, 0, false, false));
        results.add(new QuoteResult(2L, "PremiumCo", 61.25, 700, 4.8, "Feature1,Feature2,Feature3,Feature4,Feature5", null, null, 0, false, false));
        results.add(new QuoteResult(3L, "MidCo", 48.13, 550, 4.0, "Feature1,Feature2,Feature3", null, null, 0, false, false));
        return results;
    }
}
