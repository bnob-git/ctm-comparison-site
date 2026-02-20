package com.compare.controller.web;

import com.compare.config.FeatureFlagConfig;
import com.compare.config.RankingConfig;
import com.compare.domain.Provider;
import com.compare.service.ProviderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class AdminController {

    private static final Logger log = LoggerFactory.getLogger(AdminController.class);

    private final ProviderService providerService;
    private final FeatureFlagConfig featureFlagConfig;
    private final RankingConfig rankingConfig;

    public AdminController(ProviderService providerService,
                           FeatureFlagConfig featureFlagConfig,
                           RankingConfig rankingConfig) {
        this.providerService = providerService;
        this.featureFlagConfig = featureFlagConfig;
        this.rankingConfig = rankingConfig;
    }

    @GetMapping("/admin")
    public String adminPage(Model model) {
        List<Provider> providers = providerService.getAllProviders();
        model.addAttribute("providers", providers);
        model.addAttribute("experimentalRanking", featureFlagConfig.isExperimentalRanking());
        model.addAttribute("rankingConfig", rankingConfig);
        return "admin";
    }

    @PostMapping("/admin/toggle-ranking")
    public String toggleRanking(@RequestParam(defaultValue = "false") boolean enabled,
                                RedirectAttributes redirectAttributes) {
        featureFlagConfig.setExperimentalRanking(enabled);
        log.info("Experimental ranking toggled to: {}", enabled);
        redirectAttributes.addFlashAttribute("message",
                "Experimental ranking " + (enabled ? "enabled" : "disabled"));
        return "redirect:/admin";
    }
}
