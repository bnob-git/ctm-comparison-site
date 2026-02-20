package com.compare.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "ranking")
public class RankingConfig {

    private double priceWeight = 0.5;
    private double ratingWeight = 0.3;
    private double featureWeight = 0.2;

    public double getPriceWeight() { return priceWeight; }
    public void setPriceWeight(double priceWeight) { this.priceWeight = priceWeight; }

    public double getRatingWeight() { return ratingWeight; }
    public void setRatingWeight(double ratingWeight) { this.ratingWeight = ratingWeight; }

    public double getFeatureWeight() { return featureWeight; }
    public void setFeatureWeight(double featureWeight) { this.featureWeight = featureWeight; }
}
