package com.compare.dto;

import java.util.List;

public class QuoteResultDto {

    private String quoteId;
    private List<QuoteItemDto> results;
    private int totalResults;
    private String sortedBy;

    public QuoteResultDto() {}

    public String getQuoteId() { return quoteId; }
    public void setQuoteId(String quoteId) { this.quoteId = quoteId; }

    public List<QuoteItemDto> getResults() { return results; }
    public void setResults(List<QuoteItemDto> results) { this.results = results; }

    public int getTotalResults() { return totalResults; }
    public void setTotalResults(int totalResults) { this.totalResults = totalResults; }

    public String getSortedBy() { return sortedBy; }
    public void setSortedBy(String sortedBy) { this.sortedBy = sortedBy; }

    public static class QuoteItemDto {
        private Long providerId;
        private String providerName;
        private double monthlyPrice;
        private double annualPrice;
        private double rating;
        private String features;
        private String exclusions;
        private String coverLevel;
        private double score;
        private boolean bestPrice;
        private boolean recommended;

        public Long getProviderId() { return providerId; }
        public void setProviderId(Long providerId) { this.providerId = providerId; }

        public String getProviderName() { return providerName; }
        public void setProviderName(String providerName) { this.providerName = providerName; }

        public double getMonthlyPrice() { return monthlyPrice; }
        public void setMonthlyPrice(double monthlyPrice) { this.monthlyPrice = monthlyPrice; }

        public double getAnnualPrice() { return annualPrice; }
        public void setAnnualPrice(double annualPrice) { this.annualPrice = annualPrice; }

        public double getRating() { return rating; }
        public void setRating(double rating) { this.rating = rating; }

        public String getFeatures() { return features; }
        public void setFeatures(String features) { this.features = features; }

        public String getExclusions() { return exclusions; }
        public void setExclusions(String exclusions) { this.exclusions = exclusions; }

        public String getCoverLevel() { return coverLevel; }
        public void setCoverLevel(String coverLevel) { this.coverLevel = coverLevel; }

        public double getScore() { return score; }
        public void setScore(double score) { this.score = score; }

        public boolean isBestPrice() { return bestPrice; }
        public void setBestPrice(boolean bestPrice) { this.bestPrice = bestPrice; }

        public boolean isRecommended() { return recommended; }
        public void setRecommended(boolean recommended) { this.recommended = recommended; }
    }
}
