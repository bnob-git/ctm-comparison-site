package com.compare.controller.api;

import com.compare.dto.ProviderDto;
import com.compare.mapper.ProviderMapper;
import com.compare.service.ProviderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Providers", description = "Provider information endpoints")
public class ProviderApiController {

    private final ProviderService providerService;
    private final ProviderMapper providerMapper;

    public ProviderApiController(ProviderService providerService, ProviderMapper providerMapper) {
        this.providerService = providerService;
        this.providerMapper = providerMapper;
    }

    @GetMapping("/providers")
    @Operation(summary = "List all providers")
    public ResponseEntity<List<ProviderDto>> listProviders() {
        List<ProviderDto> providers = providerMapper.toDtoList(providerService.getAllProviders());
        return ResponseEntity.ok(providers);
    }

    @GetMapping("/providers/{id}")
    @Operation(summary = "Get provider details")
    public ResponseEntity<ProviderDto> getProvider(@PathVariable Long id) {
        return providerService.getProviderById(id)
                .map(p -> ResponseEntity.ok(providerMapper.toDto(p)))
                .orElse(ResponseEntity.notFound().build());
    }
}
