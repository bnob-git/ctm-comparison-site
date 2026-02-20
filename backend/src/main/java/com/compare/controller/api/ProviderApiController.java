package com.compare.controller.api;

import com.compare.domain.Provider;
import com.compare.service.ProviderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@Tag(name = "Providers", description = "Provider information endpoints")
public class ProviderApiController {

    private final ProviderService providerService;

    public ProviderApiController(ProviderService providerService) {
        this.providerService = providerService;
    }

    @GetMapping("/providers")
    @Operation(summary = "List all providers")
    public ResponseEntity<List<Map<String, Object>>> listProviders() {
        List<Map<String, Object>> providers = providerService.getAllProviders().stream()
                .map(this::toMap)
                .collect(Collectors.toList());
        return ResponseEntity.ok(providers);
    }

    @GetMapping("/providers/{id}")
    @Operation(summary = "Get provider details")
    public ResponseEntity<?> getProvider(@PathVariable Long id) {
        return providerService.getProviderById(id)
                .map(p -> ResponseEntity.ok(toMap(p)))
                .orElse(ResponseEntity.notFound().build());
    }

    private Map<String, Object> toMap(Provider p) {
        return Map.of(
                "id", p.getId(),
                "name", p.getName(),
                "baseRate", p.getBaseRate(),
                "riskMultiplier", p.getRiskMultiplier(),
                "fixedFee", p.getFixedFee(),
                "rating", p.getRating(),
                "features", p.getFeatures() != null ? p.getFeatures() : "",
                "exclusions", p.getExclusions() != null ? p.getExclusions() : "",
                "assumptions", p.getAssumptions() != null ? p.getAssumptions() : ""
        );
    }
}
