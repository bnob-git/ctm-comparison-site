package com.compare.service;

import com.compare.domain.CoverageOption;
import com.compare.domain.Provider;
import com.compare.domain.QuoteRequest;
import com.compare.domain.QuoteResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class DefaultPricingEngine implements PricingEngine {

    private static final Logger log = LoggerFactory.getLogger(DefaultPricingEngine.class);

    @Override
    public List<QuoteResult> calculateQuotes(QuoteRequest request, List<Provider> providers) {
        log.info("Calculating quotes for driverAge={}, carValue={}, mileage={}, claims={}, cover={}",
                request.getDriverAge(), request.getCarValue(), request.getAnnualMileage(),
                request.getClaimsInLastFiveYears(), request.getCoverLevel());

        List<QuoteResult> results = new ArrayList<>();

        for (Provider provider : providers) {
            double coverMultiplier = getCoverMultiplier(provider, request.getCoverLevel());
            if (coverMultiplier <= 0) {
                log.debug("Provider {} does not offer cover level {}", provider.getName(), request.getCoverLevel());
                continue;
            }

            double ageFactor = calculateAgeFactor(request.getDriverAge());
            double mileageFactor = calculateMileageFactor(request.getAnnualMileage());
            double claimsFactor = calculateClaimsFactor(request.getClaimsInLastFiveYears());
            double carValueFactor = calculateCarValueFactor(request.getCarValue());

            double annualPrice = provider.getBaseRate()
                    * ageFactor
                    * mileageFactor
                    * claimsFactor
                    * carValueFactor
                    * coverMultiplier
                    * provider.getRiskMultiplier()
                    + provider.getFixedFee();

            // Small deterministic noise based on provider ID and driver age
            Random seededRandom = new Random(provider.getId() * 1000L + request.getDriverAge());
            double noise = 1.0 + (seededRandom.nextDouble() - 0.5) * 0.04; // +/- 2%
            annualPrice = Math.round(annualPrice * noise * 100.0) / 100.0;

            double monthlyPrice = Math.round((annualPrice / 12.0 * 1.05) * 100.0) / 100.0;

            QuoteResult result = new QuoteResult();
            result.setProviderId(provider.getId());
            result.setProviderName(provider.getName());
            result.setAnnualPrice(annualPrice);
            result.setMonthlyPrice(monthlyPrice);
            result.setRating(provider.getRating());
            result.setFeatures(provider.getFeatures());
            result.setExclusions(provider.getExclusions());
            result.setCoverLevel(request.getCoverLevel());

            log.debug("Provider {}: annual={}, monthly={}", provider.getName(), annualPrice, monthlyPrice);
            results.add(result);
        }

        return results;
    }

    private double calculateAgeFactor(int age) {
        if (age < 21) return 2.0;
        if (age < 25) return 1.5;
        if (age < 30) return 1.1;
        if (age <= 65) return 1.0;
        if (age <= 75) return 1.3;
        return 1.6;
    }

    private double calculateMileageFactor(int mileage) {
        if (mileage <= 5000) return 0.8;
        if (mileage <= 8000) return 0.9;
        if (mileage <= 12000) return 1.0;
        if (mileage <= 20000) return 1.15;
        if (mileage <= 30000) return 1.3;
        return 1.5;
    }

    private double calculateClaimsFactor(int claims) {
        return 1.0 + claims * 0.2;
    }

    private double calculateCarValueFactor(double carValue) {
        if (carValue < 5000) return 0.8;
        if (carValue < 15000) return 1.0;
        if (carValue < 30000) return 1.2;
        if (carValue < 50000) return 1.4;
        return 1.6;
    }

    private double getCoverMultiplier(Provider provider, String coverLevel) {
        for (CoverageOption option : provider.getCoverageOptions()) {
            if (option.getCoverLevel().equals(coverLevel)) {
                return option.getCoverMultiplier();
            }
        }
        return 0;
    }
}
