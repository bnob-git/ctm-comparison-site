package com.compare.controller.web;

import com.compare.domain.QuoteRequest;
import com.compare.domain.QuoteResult;
import com.compare.dto.QuoteRequestDto;
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

    public QuoteWebController(QuoteService quoteService) {
        this.quoteService = quoteService;
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

        QuoteRequest request = new QuoteRequest(
                dto.getDriverAge(),
                dto.getCarValue(),
                dto.getPostcode(),
                dto.getAnnualMileage(),
                dto.getClaimsInLastFiveYears(),
                dto.getCoverLevel()
        );

        List<QuoteResult> results = quoteService.getQuotes(request);

        model.addAttribute("results", results);
        model.addAttribute("quoteRequest", dto);
        model.addAttribute("totalResults", results.size());
        return "results";
    }

    @GetMapping("/apply")
    public String showApplicationPlaceholder() {
        return "application-placeholder";
    }
}
