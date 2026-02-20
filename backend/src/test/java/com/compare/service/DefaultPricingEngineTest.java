package com.compare.service;

import com.compare.domain.CoverageOption;
import com.compare.domain.Provider;
import com.compare.domain.QuoteRequest;
import com.compare.domain.QuoteResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DefaultPricingEngineTest {

    private DefaultPricingEngine pricingEngine;

    @BeforeEach
    void setUp() {
        pricingEngine = new DefaultPricingEngine();
    }

    @Test
    void shouldReturnQuotesForAllProvidersWithMatchingCover() {
        List<Provider> providers = List.of(createProvider(1L, "TestProvider", 300, 1.0, 30, 4.0));
        QuoteRequest request = new QuoteRequest(30, 15000, "SW1A 1AA", 10000, 0, "comprehensive");

        List<QuoteResult> results = pricingEngine.calculateQuotes(request, providers);

        assertEquals(1, results.size());
        assertEquals("TestProvider", results.get(0).getProviderName());
        assertTrue(results.get(0).getAnnualPrice() > 0);
        assertTrue(results.get(0).getMonthlyPrice() > 0);
    }

    @Test
    void shouldSkipProviderWithoutMatchingCoverLevel() {
        Provider provider = createProvider(1L, "TestProvider", 300, 1.0, 30, 4.0);
        provider.getCoverageOptions().clear();
        provider.addCoverageOption(new CoverageOption("third_party", 1.0, "Basic"));

        QuoteRequest request = new QuoteRequest(30, 15000, "SW1A 1AA", 10000, 0, "comprehensive");
        List<QuoteResult> results = pricingEngine.calculateQuotes(request, List.of(provider));

        assertTrue(results.isEmpty());
    }

    @Test
    void youngerDriversShouldPayMore() {
        List<Provider> providers = List.of(createProvider(1L, "TestProvider", 300, 1.0, 30, 4.0));

        QuoteRequest youngDriver = new QuoteRequest(19, 15000, "SW1A 1AA", 10000, 0, "comprehensive");
        QuoteRequest olderDriver = new QuoteRequest(35, 15000, "SW1A 1AA", 10000, 0, "comprehensive");

        List<QuoteResult> youngResults = pricingEngine.calculateQuotes(youngDriver, providers);
        List<QuoteResult> olderResults = pricingEngine.calculateQuotes(olderDriver, providers);

        assertTrue(youngResults.get(0).getAnnualPrice() > olderResults.get(0).getAnnualPrice(),
                "Young drivers should pay more");
    }

    @Test
    void moreClaimsShouldIncreasePrice() {
        List<Provider> providers = List.of(createProvider(1L, "TestProvider", 300, 1.0, 30, 4.0));

        QuoteRequest noClaims = new QuoteRequest(30, 15000, "SW1A 1AA", 10000, 0, "comprehensive");
        QuoteRequest twoClaims = new QuoteRequest(30, 15000, "SW1A 1AA", 10000, 2, "comprehensive");

        List<QuoteResult> noClaimsResults = pricingEngine.calculateQuotes(noClaims, providers);
        List<QuoteResult> twoClaimsResults = pricingEngine.calculateQuotes(twoClaims, providers);

        assertTrue(twoClaimsResults.get(0).getAnnualPrice() > noClaimsResults.get(0).getAnnualPrice(),
                "More claims should increase price");
    }

    @Test
    void higherMileageShouldIncreasePrice() {
        List<Provider> providers = List.of(createProvider(1L, "TestProvider", 300, 1.0, 30, 4.0));

        QuoteRequest lowMileage = new QuoteRequest(30, 15000, "SW1A 1AA", 5000, 0, "comprehensive");
        QuoteRequest highMileage = new QuoteRequest(30, 15000, "SW1A 1AA", 25000, 0, "comprehensive");

        List<QuoteResult> lowResults = pricingEngine.calculateQuotes(lowMileage, providers);
        List<QuoteResult> highResults = pricingEngine.calculateQuotes(highMileage, providers);

        assertTrue(highResults.get(0).getAnnualPrice() > lowResults.get(0).getAnnualPrice(),
                "Higher mileage should increase price");
    }

    @Test
    void monthlyPriceShouldIncludeSurcharge() {
        List<Provider> providers = List.of(createProvider(1L, "TestProvider", 300, 1.0, 30, 4.0));
        QuoteRequest request = new QuoteRequest(30, 15000, "SW1A 1AA", 10000, 0, "comprehensive");

        List<QuoteResult> results = pricingEngine.calculateQuotes(request, providers);
        QuoteResult result = results.get(0);

        double expectedMonthly = Math.round((result.getAnnualPrice() / 12.0 * 1.05) * 100.0) / 100.0;
        assertEquals(expectedMonthly, result.getMonthlyPrice(), 0.01);
    }

    @Test
    void pricingShouldBeDeterministic() {
        List<Provider> providers = List.of(createProvider(1L, "TestProvider", 300, 1.0, 30, 4.0));
        QuoteRequest request = new QuoteRequest(30, 15000, "SW1A 1AA", 10000, 0, "comprehensive");

        List<QuoteResult> results1 = pricingEngine.calculateQuotes(request, providers);
        List<QuoteResult> results2 = pricingEngine.calculateQuotes(request, providers);

        assertEquals(results1.get(0).getAnnualPrice(), results2.get(0).getAnnualPrice(),
                "Same inputs should produce same prices (deterministic)");
    }

    private Provider createProvider(Long id, String name, double baseRate, double riskMultiplier,
                                     double fixedFee, double rating) {
        Provider provider = new Provider(name, baseRate, riskMultiplier, fixedFee, rating,
                "Feature1,Feature2", "Exclusion1", "Assumption1");
        provider.setId(id);
        provider.addCoverageOption(new CoverageOption("third_party", 1.0, "Basic"));
        provider.addCoverageOption(new CoverageOption("tp_fire_theft", 1.2, "Mid"));
        provider.addCoverageOption(new CoverageOption("comprehensive", 1.5, "Full"));
        return provider;
    }
}
