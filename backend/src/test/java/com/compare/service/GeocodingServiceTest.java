package com.compare.service;

import com.compare.service.GeocodingService.GeoLocation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GeocodingServiceTest {

    private final GeocodingService service = new GeocodingService();

    @Test
    void geocode_validPostcode_returnsCoords() {
        GeoLocation loc = service.geocode("SW1A 1AA");
        assertNotNull(loc);
        assertEquals(51.4613, loc.latitude(), 0.01);
        assertEquals(-0.1685, loc.longitude(), 0.01);
    }

    @Test
    void geocode_caseInsensitive() {
        GeoLocation loc = service.geocode("sw1a 1aa");
        assertNotNull(loc);
        assertEquals(51.4613, loc.latitude(), 0.01);
    }

    @Test
    void geocode_noSpaceNormalisation() {
        GeoLocation loc1 = service.geocode("SW1A1AA");
        GeoLocation loc2 = service.geocode("SW1A 1AA");
        assertNotNull(loc1);
        assertNotNull(loc2);
        assertEquals(loc1.latitude(), loc2.latitude(), 0.001);
        assertEquals(loc1.longitude(), loc2.longitude(), 0.001);
    }

    @Test
    void geocode_unknownPrefix_returnsLondonDefault() {
        GeoLocation loc = service.geocode("ZZ1 1AA");
        assertNotNull(loc);
        assertEquals(51.5074, loc.latitude(), 0.01);
        assertEquals(-0.1278, loc.longitude(), 0.01);
    }

    @Test
    void geocode_null_returnsNull() {
        assertNull(service.geocode(null));
    }

    @Test
    void geocode_empty_returnsNull() {
        assertNull(service.geocode(""));
        assertNull(service.geocode("   "));
    }

    @Test
    void geocode_differentPrefixes_returnDifferentCoords() {
        GeoLocation london = service.geocode("SW1A 1AA");
        GeoLocation manchester = service.geocode("M1 1AA");
        GeoLocation edinburgh = service.geocode("EH1 1AA");

        assertNotNull(london);
        assertNotNull(manchester);
        assertNotNull(edinburgh);

        // They should all be different
        assertNotEquals(london.latitude(), manchester.latitude(), 0.1);
        assertNotEquals(london.latitude(), edinburgh.latitude(), 0.1);
        assertNotEquals(manchester.latitude(), edinburgh.latitude(), 0.1);
    }
}
