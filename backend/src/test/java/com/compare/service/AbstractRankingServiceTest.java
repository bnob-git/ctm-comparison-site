package com.compare.service;

import com.compare.config.RankingConfig;
import com.compare.domain.QuoteResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AbstractRankingServiceTest {

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
    void rank_emptyInput_returnsEmpty() {
        List<QuoteResult> results = rankingService.rank(new ArrayList<>());
        assertTrue(results.isEmpty());
    }

    @Test
    void rank_sortsByScoreDescending() {
        QuoteResult r1 = createResult(1L, "Cheap", 200.0, 3.0, "F1");
        QuoteResult r2 = createResult(2L, "Expensive", 500.0, 5.0, "F1,F2,F3,F4,F5");

        List<QuoteResult> results = new ArrayList<>(List.of(r1, r2));
        results = rankingService.rank(results);

        // Both should have scores set
        assertTrue(results.get(0).getScore() >= results.get(1).getScore());
    }

    @Test
    void rank_marksBestPrice() {
        QuoteResult r1 = createResult(1L, "Cheap", 200.0, 3.0, "F1");
        QuoteResult r2 = createResult(2L, "Expensive", 500.0, 5.0, "F1,F2");

        List<QuoteResult> results = new ArrayList<>(List.of(r1, r2));
        results = rankingService.rank(results);

        // The cheapest should be marked as best price
        QuoteResult cheapest = results.stream()
                .filter(QuoteResult::getIsBestPrice)
                .findFirst()
                .orElse(null);
        assertNotNull(cheapest);
        assertEquals(200.0, cheapest.getAnnualPrice());
    }

    @Test
    void rank_marksRecommended() {
        QuoteResult r1 = createResult(1L, "A", 300.0, 4.0, "F1,F2");
        QuoteResult r2 = createResult(2L, "B", 400.0, 3.5, "F1");

        List<QuoteResult> results = new ArrayList<>(List.of(r1, r2));
        results = rankingService.rank(results);

        // First result (highest score) should be recommended
        assertTrue(results.get(0).getIsRecommended());
    }

    private QuoteResult createResult(Long id, String name, double price, double rating, String features) {
        QuoteResult r = new QuoteResult();
        r.setProviderId(id);
        r.setProviderName(name);
        r.setAnnualPrice(price);
        r.setMonthlyPrice(price / 12.0);
        r.setRating(rating);
        r.setFeatures(features);
        r.setCoverLevel("comprehensive");
        return r;
    }
}
