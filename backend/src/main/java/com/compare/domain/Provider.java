package com.compare.domain;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "providers")
public class Provider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private double baseRate;

    @Column(nullable = false)
    private double riskMultiplier;

    @Column(nullable = false)
    private double fixedFee;

    @Column(nullable = false)
    private double rating;

    @Column(length = 1000)
    private String features;

    @Column(length = 1000)
    private String exclusions;

    @Column(length = 500)
    private String assumptions;

    @Column(length = 200)
    private String logoPlaceholder;

    @OneToMany(mappedBy = "provider", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CoverageOption> coverageOptions = new ArrayList<>();

    public Provider() {}

    public Provider(String name, double baseRate, double riskMultiplier, double fixedFee,
                    double rating, String features, String exclusions, String assumptions) {
        this.name = name;
        this.baseRate = baseRate;
        this.riskMultiplier = riskMultiplier;
        this.fixedFee = fixedFee;
        this.rating = rating;
        this.features = features;
        this.exclusions = exclusions;
        this.assumptions = assumptions;
    }

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

    public String getLogoPlaceholder() { return logoPlaceholder; }
    public void setLogoPlaceholder(String logoPlaceholder) { this.logoPlaceholder = logoPlaceholder; }

    public List<CoverageOption> getCoverageOptions() { return coverageOptions; }
    public void setCoverageOptions(List<CoverageOption> coverageOptions) { this.coverageOptions = coverageOptions; }

    public void addCoverageOption(CoverageOption option) {
        coverageOptions.add(option);
        option.setProvider(this);
    }
}
