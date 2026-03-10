package com.compare.dto;

import java.util.List;

public class ProviderDto {

    private Long id;
    private String name;
    private double baseRate;
    private double riskMultiplier;
    private double fixedFee;
    private double rating;
    private String features;
    private String exclusions;
    private String assumptions;
    private Double latitude;
    private Double longitude;
    private List<CoverageOptionDto> coverageOptions;

    public ProviderDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getBaseRate() { return baseRate; }
    public void setBaseRate(double baseRate) { this.baseRate = baseRate; }

    public double getRiskMultiplier() { return riskMultiplier; }
    public void setRiskMultiplier(double riskMultiplier) { this.riskMultiplier = riskMultiplier; }

    public double getFixedFee() { return fixedFee; }
    public void setFixedFee(double fixedFee) { this.fixedFee = fixedFee; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public String getFeatures() { return features; }
    public void setFeatures(String features) { this.features = features; }

    public String getExclusions() { return exclusions; }
    public void setExclusions(String exclusions) { this.exclusions = exclusions; }

    public String getAssumptions() { return assumptions; }
    public void setAssumptions(String assumptions) { this.assumptions = assumptions; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public List<CoverageOptionDto> getCoverageOptions() { return coverageOptions; }
    public void setCoverageOptions(List<CoverageOptionDto> coverageOptions) { this.coverageOptions = coverageOptions; }
}
