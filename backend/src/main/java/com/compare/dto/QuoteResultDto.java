package com.compare.dto;

import java.util.List;

public record QuoteResultDto(
        String quoteId,
        List<QuoteItemDto> results,
        int totalResults,
        String sortedBy
) {
    public record QuoteItemDto(
            Long providerId,
            String providerName,
            double monthlyPrice,
            double annualPrice,
            double rating,
            String features,
            String exclusions,
            String coverLevel,
            double score,
            boolean bestPrice,
            boolean recommended
    ) {}
}
