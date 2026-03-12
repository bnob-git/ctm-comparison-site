package com.compare.controller.api;

import com.compare.config.FeatureFlagConfig;
import com.compare.dto.ProviderLocationDto;
import com.compare.mapper.ProviderMapper;
import com.compare.service.DistanceUtil;
import com.compare.service.GeocodingService;
import com.compare.service.GeocodingService.GeoLocation;
import com.compare.service.ProviderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/map")
@Tag(name = "Map", description = "Geolocated map endpoints")
public class MapApiController {

    private final ProviderService providerService;
    private final ProviderMapper providerMapper;
    private final GeocodingService geocodingService;
    private final FeatureFlagConfig featureFlagConfig;

    public MapApiController(ProviderService providerService, ProviderMapper providerMapper,
                            GeocodingService geocodingService, FeatureFlagConfig featureFlagConfig) {
        this.providerService = providerService;
        this.providerMapper = providerMapper;
        this.geocodingService = geocodingService;
        this.featureFlagConfig = featureFlagConfig;
    }

    @GetMapping("/providers")
    @Operation(summary = "List provider locations for map display")
    public ResponseEntity<?> getProviderLocations(
            @RequestParam(required = false) String postcode,
            @RequestParam(required = false) Double radius) {

        if (!featureFlagConfig.isMapEnabled()) {
            return ResponseEntity.notFound().build();
        }

        List<ProviderLocationDto> locations = providerMapper.toLocationDtoList(
                providerService.getAllProviders());

        if (postcode != null && !postcode.isBlank() && radius != null && radius > 0) {
            GeoLocation userLocation = geocodingService.geocode(postcode);
            if (userLocation != null) {
                locations = locations.stream()
                        .filter(loc -> DistanceUtil.isWithinRadius(
                                userLocation.latitude(), userLocation.longitude(),
                                loc.getLatitude(), loc.getLongitude(), radius))
                        .collect(Collectors.toList());
            }
        }

        return ResponseEntity.ok(locations);
    }

    @GetMapping("/geocode")
    @Operation(summary = "Geocode a UK postcode")
    public ResponseEntity<?> geocodePostcode(@RequestParam String postcode) {
        if (!featureFlagConfig.isMapEnabled()) {
            return ResponseEntity.notFound().build();
        }

        if (postcode == null || postcode.isBlank()) {
            return ResponseEntity.badRequest().body(
                    java.util.Map.of("error", "Postcode is required"));
        }

        GeoLocation location = geocodingService.geocode(postcode);
        if (location == null) {
            return ResponseEntity.badRequest().body(
                    java.util.Map.of("error", "Unable to geocode postcode"));
        }

        return ResponseEntity.ok(java.util.Map.of(
                "latitude", location.latitude(),
                "longitude", location.longitude()));
    }
}
