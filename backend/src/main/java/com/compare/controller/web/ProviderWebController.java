package com.compare.controller.web;

import com.compare.config.FeatureFlagConfig;
import com.compare.domain.Provider;
import com.compare.service.ProviderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
public class ProviderWebController {

    private final ProviderService providerService;
    private final FeatureFlagConfig featureFlagConfig;

    public ProviderWebController(ProviderService providerService, FeatureFlagConfig featureFlagConfig) {
        this.providerService = providerService;
        this.featureFlagConfig = featureFlagConfig;
    }

    @GetMapping("/provider/{id}")
    public String providerDetail(@PathVariable Long id, Model model) {
        Optional<Provider> provider = providerService.getProviderById(id);
        if (provider.isEmpty()) {
            return "redirect:/";
        }
        model.addAttribute("provider", provider.get());
        model.addAttribute("mapEnabled", featureFlagConfig.isMapEnabled());
        return "provider-detail";
    }
}
