package com.compare.dto;

public class CoverageOptionDto {

    private Long id;
    private String coverLevel;
    private double coverMultiplier;
    private String description;

    public CoverageOptionDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCoverLevel() { return coverLevel; }
    public void setCoverLevel(String coverLevel) { this.coverLevel = coverLevel; }

    public double getCoverMultiplier() { return coverMultiplier; }
    public void setCoverMultiplier(double coverMultiplier) { this.coverMultiplier = coverMultiplier; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
