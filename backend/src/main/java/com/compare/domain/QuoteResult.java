package com.compare.domain;

public class QuoteResult {

    private Long providerId;
    private String providerName;
    private double monthlyPrice;
    private double annualPrice;
    private double rating;
    private String features;
    private String exclusions;
    private String coverLevel;
    private double score;
    private boolean isBestPrice;
    private boolean isRecommended;

    public QuoteResult() {}

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

    public boolean getIsBestPrice() { return isBestPrice; }
    public void setIsBestPrice(boolean bestPrice) { isBestPrice = bestPrice; }

    public boolean getIsRecommended() { return isRecommended; }
    public void setIsRecommended(boolean recommended) { isRecommended = recommended; }
}
