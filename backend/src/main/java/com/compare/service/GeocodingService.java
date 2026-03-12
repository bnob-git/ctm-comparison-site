package com.compare.service;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class GeocodingService {

    public record GeoLocation(double latitude, double longitude) {}

    private static final Map<String, GeoLocation> POSTCODE_LOOKUP = Map.ofEntries(
            Map.entry("EC", new GeoLocation(51.5155, -0.0922)),
            Map.entry("WC", new GeoLocation(51.5165, -0.1200)),
            Map.entry("E", new GeoLocation(51.5450, -0.0553)),
            Map.entry("N", new GeoLocation(51.5722, -0.1028)),
            Map.entry("NW", new GeoLocation(51.5472, -0.1736)),
            Map.entry("SE", new GeoLocation(51.4500, -0.0500)),
            Map.entry("SW", new GeoLocation(51.4613, -0.1685)),
            Map.entry("W", new GeoLocation(51.5100, -0.2059)),
            Map.entry("BR", new GeoLocation(51.4039, 0.0198)),
            Map.entry("CR", new GeoLocation(51.3714, -0.0977)),
            Map.entry("DA", new GeoLocation(51.4400, 0.1500)),
            Map.entry("EN", new GeoLocation(51.6522, -0.0808)),
            Map.entry("HA", new GeoLocation(51.5836, -0.3464)),
            Map.entry("IG", new GeoLocation(51.5579, 0.0786)),
            Map.entry("KT", new GeoLocation(51.3780, -0.2920)),
            Map.entry("RM", new GeoLocation(51.5700, 0.1800)),
            Map.entry("SM", new GeoLocation(51.3618, -0.1945)),
            Map.entry("TW", new GeoLocation(51.4490, -0.3270)),
            Map.entry("UB", new GeoLocation(51.5400, -0.4400)),
            Map.entry("WD", new GeoLocation(51.6800, -0.3200)),
            Map.entry("B", new GeoLocation(52.4862, -1.8904)),
            Map.entry("M", new GeoLocation(53.4808, -2.2426)),
            Map.entry("L", new GeoLocation(53.4084, -2.9916)),
            Map.entry("LS", new GeoLocation(53.8008, -1.5491)),
            Map.entry("S", new GeoLocation(53.3811, -1.4701)),
            Map.entry("BS", new GeoLocation(51.4545, -2.5879)),
            Map.entry("EH", new GeoLocation(55.9533, -3.1883)),
            Map.entry("G", new GeoLocation(55.8642, -4.2518)),
            Map.entry("CF", new GeoLocation(51.4816, -3.1791)),
            Map.entry("CB", new GeoLocation(52.2053, 0.1218))
    );

    // Default: London centre
    private static final GeoLocation DEFAULT_LOCATION = new GeoLocation(51.5074, -0.1278);

    /**
     * Geocode a UK postcode to approximate coordinates based on outward code prefix.
     * Returns null for null/empty input, London centre for unknown prefixes.
     */
    public GeoLocation geocode(String postcode) {
        if (postcode == null || postcode.isBlank()) {
            return null;
        }

        String normalized = postcode.trim().toUpperCase().replaceAll("\\s+", "");

        // Extract outward code prefix (letters before the first digit)
        StringBuilder prefix = new StringBuilder();
        for (char c : normalized.toCharArray()) {
            if (Character.isLetter(c)) {
                prefix.append(c);
            } else {
                break;
            }
        }

        String prefixStr = prefix.toString();
        if (prefixStr.isEmpty()) {
            return DEFAULT_LOCATION;
        }

        return POSTCODE_LOOKUP.getOrDefault(prefixStr, DEFAULT_LOCATION);
    }
}
