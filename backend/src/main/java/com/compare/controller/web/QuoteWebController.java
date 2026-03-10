package com.compare.controller.web;

import com.compare.config.FeatureFlagConfig;
import com.compare.domain.QuoteResult;
import com.compare.dto.QuoteRequestDto;
import com.compare.mapper.QuoteMapper;
import com.compare.service.GeocodingService;
import com.compare.service.GeocodingService.GeoLocation;
import com.compare.service.QuoteService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class QuoteWebController {

    private static final Logger log = LoggerFactory.getLogger(QuoteWebController.class);

    private final QuoteService quoteService;
    private final QuoteMapper quoteMapper;
    private final GeocodingService geocodingService;
    private final FeatureFlagConfig featureFlagConfig;

    public QuoteWebController(QuoteService quoteService, QuoteMapper quoteMapper,
                              GeocodingService geocodingService, FeatureFlagConfig featureFlagConfig) {
        this.quoteService = quoteService;
        this.quoteMapper = quoteMapper;
        this.geocodingService = geocodingService;
        this.featureFlagConfig = featureFlagConfig;
    }

    @GetMapping("/quote")
    public String showQuoteForm(Model model) {
        model.addAttribute("quoteRequest", new QuoteRequestDto());
        return "quote-form";
    }

    @PostMapping("/quote")
    public String submitQuote(@Valid @ModelAttribute("quoteRequest") QuoteRequestDto dto,
                              BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            log.warn("Quote form validation errors: {}", bindingResult.getAllErrors());
            return "quote-form";
        }

        List<QuoteResult> results = quoteService.getQuotes(quoteMapper.toQuoteRequest(dto));

        model.addAttribute("results", results);
        model.addAttribute("quoteRequest", dto);
        model.addAttribute("totalResults", results.size());

        boolean mapEnabled = featureFlagConfig.isMapEnabled();
        model.addAttribute("mapEnabled", mapEnabled);

        if (mapEnabled) {
            GeoLocation userLocation = geocodingService.geocode(dto.getPostcode());
            if (userLocation != null) {
                model.addAttribute("userLat", userLocation.latitude());
                model.addAttribute("userLng", userLocation.longitude());
            }
        }

        return "results";
    }

    @GetMapping("/apply")
    public String showApplicationPlaceholder() {
        return "application-placeholder";
    }
}
