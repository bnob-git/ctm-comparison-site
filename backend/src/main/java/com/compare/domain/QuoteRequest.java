package com.compare.domain;

public class QuoteRequest {

    private int driverAge;
    private double carValue;
    private String postcode;
    private int annualMileage;
    private int claimsInLastFiveYears;
    private String coverLevel;

    public QuoteRequest() {}

    public QuoteRequest(int driverAge, double carValue, String postcode,
                        int annualMileage, int claimsInLastFiveYears, String coverLevel) {
        this.driverAge = driverAge;
        this.carValue = carValue;
        this.postcode = postcode;
        this.annualMileage = annualMileage;
        this.claimsInLastFiveYears = claimsInLastFiveYears;
        this.coverLevel = coverLevel;
    }

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
