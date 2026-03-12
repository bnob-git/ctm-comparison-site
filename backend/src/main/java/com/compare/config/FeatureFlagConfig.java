package com.compare.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "feature")
public class FeatureFlagConfig {

    private boolean experimentalRanking = false;
    private boolean mapEnabled = false;

    public boolean isExperimentalRanking() { return experimentalRanking; }
    public void setExperimentalRanking(boolean experimentalRanking) {
        this.experimentalRanking = experimentalRanking;
    }

    public boolean isMapEnabled() { return mapEnabled; }
    public void setMapEnabled(boolean mapEnabled) {
        this.mapEnabled = mapEnabled;
    }
}
