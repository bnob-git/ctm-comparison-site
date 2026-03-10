package com.compare.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FeatureFlagConfigTest {

    @Test
    void mapEnabled_defaultIsFalse() {
        FeatureFlagConfig config = new FeatureFlagConfig();
        assertFalse(config.isMapEnabled());
    }

    @Test
    void mapEnabled_getterReturnsConfiguredValue() {
        FeatureFlagConfig config = new FeatureFlagConfig();
        config.setMapEnabled(true);
        assertTrue(config.isMapEnabled());
    }

    @Test
    void experimentalRanking_defaultIsFalse() {
        FeatureFlagConfig config = new FeatureFlagConfig();
        assertFalse(config.isExperimentalRanking());
    }
}
