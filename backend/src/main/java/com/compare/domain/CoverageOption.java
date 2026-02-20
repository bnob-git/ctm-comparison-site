package com.compare.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "coverage_options")
public class CoverageOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String coverLevel;

    @Column(nullable = false)
    private double coverMultiplier;

    @Column(length = 500)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id", nullable = false)
    private Provider provider;

    public CoverageOption() {}

    public CoverageOption(String coverLevel, double coverMultiplier, String description) {
        this.coverLevel = coverLevel;
        this.coverMultiplier = coverMultiplier;
        this.description = description;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCoverLevel() { return coverLevel; }
    public void setCoverLevel(String coverLevel) { this.coverLevel = coverLevel; }

    public double getCoverMultiplier() { return coverMultiplier; }
    public void setCoverMultiplier(double coverMultiplier) { this.coverMultiplier = coverMultiplier; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Provider getProvider() { return provider; }
    public void setProvider(Provider provider) { this.provider = provider; }
}
