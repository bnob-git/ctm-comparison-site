package com.compare.dto;

import jakarta.validation.constraints.*;

public class QuoteRequestDto {

    @Min(value = 17, message = "Driver must be at least 17 years old")
    @Max(value = 99, message = "Driver age must be 99 or below")
    private int driverAge;

    @Min(value = 500, message = "Car value must be at least £500")
    @Max(value = 200000, message = "Car value must be £200,000 or below")
    private double carValue;

    @NotBlank(message = "Postcode is required")
    @Pattern(regexp = "^[A-Za-z]{1,2}\\d[A-Za-z\\d]?\\s?\\d[A-Za-z]{2}$",
             message = "Please enter a valid UK postcode")
    private String postcode;

    @Min(value = 0, message = "Annual mileage cannot be negative")
    @Max(value = 100000, message = "Annual mileage must be 100,000 or below")
    private int annualMileage;

    @Min(value = 0, message = "Claims cannot be negative")
    @Max(value = 10, message = "Claims must be 10 or fewer")
    private int claimsInLastFiveYears;

    @NotBlank(message = "Cover level is required")
    @Pattern(regexp = "^(third_party|tp_fire_theft|comprehensive)$",
             message = "Cover level must be third_party, tp_fire_theft, or comprehensive")
    private String coverLevel;

    public QuoteRequestDto() {}

    public int getDriverAge() { return driverAge; }
    public void setDriverAge(int driverAge) { this.driverAge = driverAge; }

    public double getCarValue() { return carValue; }
    public void setCarValue(double carValue) { this.carValue = carValue; }

    public String getPostcode() { return postcode; }
    public void setPostcode(String postcode) { this.postcode = postcode; }

    public int getAnnualMileage() { return annualMileage; }
    public void setAnnualMileage(int annualMileage) { this.annualMileage = annualMileage; }

    public int getClaimsInLastFiveYears() { return claimsInLastFiveYears; }
    public void setClaimsInLastFiveYears(int claimsInLastFiveYears) { this.claimsInLastFiveYears = claimsInLastFiveYears; }

    public String getCoverLevel() { return coverLevel; }
    public void setCoverLevel(String coverLevel) { this.coverLevel = coverLevel; }
}
