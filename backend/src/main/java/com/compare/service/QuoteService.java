package com.compare.service;

import com.compare.config.FeatureFlagConfig;
import com.compare.domain.Provider;
import com.compare.domain.QuoteRequest;
import com.compare.domain.QuoteResult;
import com.compare.repository.ProviderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class QuoteService {

    private static final Logger log = LoggerFactory.getLogger(QuoteService.class);

    private final ProviderRepository providerRepository;
    private final PricingEngine pricingEngine;
    private final RankingService defaultRankingService;
    private final RankingService experimentalRankingService;
    private final FeatureFlagConfig featureFlagConfig;

    public QuoteService(ProviderRepository providerRepository,
                        PricingEngine pricingEngine,
                        @Qualifier("defaultRankingService") RankingService defaultRankingService,
                        @Qualifier("experimentalRankingService") RankingService experimentalRankingService,
                        FeatureFlagConfig featureFlagConfig) {
        this.providerRepository = providerRepository;
        this.pricingEngine = pricingEngine;
        this.defaultRankingService = defaultRankingService;
        this.experimentalRankingService = experimentalRankingService;
        this.featureFlagConfig = featureFlagConfig;
    }

    public List<QuoteResult> getQuotes(QuoteRequest request) {
        String traceId = UUID.randomUUID().toString().substring(0, 8);
        MDC.put("traceId", traceId);

        log.info("Processing quote request: driverAge={}, carValue={}, postcode={}, mileage={}, claims={}, cover={}",
                request.getDriverAge(), request.getCarValue(), request.getPostcode(),
                request.getAnnualMileage(), request.getClaimsInLastFiveYears(), request.getCoverLevel());

        List<Provider> providers = providerRepository.findAll();
        log.info("Found {} providers", providers.size());

        List<QuoteResult> results = pricingEngine.calculateQuotes(request, providers);
        log.info("Generated {} quotes", results.size());

        RankingService rankingService = featureFlagConfig.isExperimentalRanking()
                ? experimentalRankingService
                : defaultRankingService;

        results = rankingService.rank(results);
        log.info("Ranked results using {} algorithm",
                featureFlagConfig.isExperimentalRanking() ? "experimental" : "default");

        MDC.remove("traceId");
        return results;
    }
}
