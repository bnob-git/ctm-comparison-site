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

        long bestPriceCount = ranked.stream().filter(QuoteResult::getIsBestPrice).count();
        assertEquals(1, bestPriceCount, "Exactly one result should be marked as best price");

        QuoteResult bestPrice = ranked.stream().filter(QuoteResult::getIsBestPrice).findFirst().orElseThrow();
        double minPrice = ranked.stream().mapToDouble(QuoteResult::getAnnualPrice).min().orElse(0);
        assertEquals(minPrice, bestPrice.getAnnualPrice(), "Best price should be the cheapest");
    }

    @Test
    void shouldMarkFirstRankedAsRecommended() {
        List<QuoteResult> results = createSampleResults();
        List<QuoteResult> ranked = rankingService.rank(results);

        assertTrue(ranked.get(0).getIsRecommended(), "First result should be recommended");
        long recommendedCount = ranked.stream().filter(QuoteResult::getIsRecommended).count();
        assertEquals(1, recommendedCount, "Exactly one result should be recommended");
    }

    @Test
    void shouldAssignScores() {
        List<QuoteResult> results = createSampleResults();
        List<QuoteResult> ranked = rankingService.rank(results);

        for (QuoteResult r : ranked) {
            assertTrue(r.getScore() >= 0 && r.getScore() <= 1.0,
                    "Score should be between 0 and 1: " + r.getScore());
        }
    }

    @Test
    void shouldSortByScoreDescending() {
        List<QuoteResult> results = createSampleResults();
        List<QuoteResult> ranked = rankingService.rank(results);

        for (int i = 0; i < ranked.size() - 1; i++) {
            assertTrue(ranked.get(i).getScore() >= ranked.get(i + 1).getScore(),
                    "Results should be sorted by score descending");
        }
    }

    @Test
    void shouldHandleSingleResult() {
        QuoteResult result = new QuoteResult();
        result.setProviderId(1L);
        result.setProviderName("Solo");
        result.setAnnualPrice(500);
        result.setRating(4.0);
        result.setFeatures("Feature1,Feature2");

        List<QuoteResult> ranked = rankingService.rank(new ArrayList<>(List.of(result)));

        assertEquals(1, ranked.size());
        assertTrue(ranked.get(0).getIsBestPrice());
        assertTrue(ranked.get(0).getIsRecommended());
    }

    private List<QuoteResult> createSampleResults() {
        List<QuoteResult> results = new ArrayList<>();

        QuoteResult r1 = new QuoteResult();
        r1.setProviderId(1L);
        r1.setProviderName("CheapCo");
        r1.setAnnualPrice(400);
        r1.setMonthlyPrice(35);
        r1.setRating(3.5);
        r1.setFeatures("Feature1,Feature2");
        results.add(r1);

        QuoteResult r2 = new QuoteResult();
        r2.setProviderId(2L);
        r2.setProviderName("PremiumCo");
        r2.setAnnualPrice(700);
        r2.setMonthlyPrice(61.25);
        r2.setRating(4.8);
        r2.setFeatures("Feature1,Feature2,Feature3,Feature4,Feature5");
        results.add(r2);

        QuoteResult r3 = new QuoteResult();
        r3.setProviderId(3L);
        r3.setProviderName("MidCo");
        r3.setAnnualPrice(550);
        r3.setMonthlyPrice(48.13);
        r3.setRating(4.0);
        r3.setFeatures("Feature1,Feature2,Feature3");
        results.add(r3);

        return results;
    }
}
