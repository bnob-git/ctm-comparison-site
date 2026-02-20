package com.compare.domain;

public record QuoteResult(
        Long providerId,
        String providerName,
        double monthlyPrice,
        double annualPrice,
        double rating,
        String features,
        String exclusions,
        String coverLevel,
        double score,
        boolean isBestPrice,
        boolean isRecommended
) {}
