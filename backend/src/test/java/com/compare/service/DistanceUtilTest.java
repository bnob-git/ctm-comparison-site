package com.compare.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DistanceUtilTest {

    @Test
    void haversine_londonToManchester_approximately260km() {
        // London (51.5074, -0.1278) to Manchester (53.4808, -2.2426)
        double distance = DistanceUtil.haversine(51.5074, -0.1278, 53.4808, -2.2426);
        assertEquals(260.0, distance, 20.0); // ~260km with tolerance
    }

    @Test
    void haversine_samePoint_returnsZero() {
        double distance = DistanceUtil.haversine(51.5074, -0.1278, 51.5074, -0.1278);
        assertEquals(0.0, distance, 0.001);
    }

    @Test
    void haversine_antipodal_approximately20000km() {
        // North pole to south pole
        double distance = DistanceUtil.haversine(90.0, 0.0, -90.0, 0.0);
        assertEquals(20015.0, distance, 100.0); // ~20015km
    }

    @Test
    void isWithinRadius_true() {
        // London to nearby point (~10km)
        assertTrue(DistanceUtil.isWithinRadius(51.5074, -0.1278, 51.55, -0.1, 20.0));
    }

    @Test
    void isWithinRadius_false() {
        // London to Manchester (~260km) with 100km radius
        assertFalse(DistanceUtil.isWithinRadius(51.5074, -0.1278, 53.4808, -2.2426, 100.0));
    }
}
