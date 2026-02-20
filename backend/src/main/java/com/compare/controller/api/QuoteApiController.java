package com.compare.controller.api;

import com.compare.domain.QuoteRequest;
import com.compare.domain.QuoteResult;
import com.compare.dto.QuoteRequestDto;
import com.compare.dto.QuoteResultDto;
import com.compare.service.QuoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@Tag(name = "Quotes", description = "Quote comparison endpoints")
public class QuoteApiController {

    private static final Logger log = LoggerFactory.getLogger(QuoteApiController.class);

    private final QuoteService quoteService;

    public QuoteApiController(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    @PostMapping("/quotes")
    @Operation(summary = "Get insurance quotes", description = "Submit quote request and receive ranked provider quotes")
    public ResponseEntity<?> getQuotes(@Valid @RequestBody QuoteRequestDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(e -> errors.put(e.getField(), e.getDefaultMessage()));
            log.warn("Validation errors in quote request: {}", errors);
            return ResponseEntity.badRequest().body(Map.of("errors", errors));
        }

        QuoteRequest request = mapToQuoteRequest(dto);
        List<QuoteResult> results = quoteService.getQuotes(request);

        QuoteResultDto response = new QuoteResultDto();
        response.setQuoteId(UUID.randomUUID().toString().substring(0, 8));
        response.setTotalResults(results.size());
        response.setSortedBy("score");
        response.setResults(results.stream().map(this::mapToItemDto).collect(Collectors.toList()));

        return ResponseEntity.ok(response);
    }

    private QuoteRequest mapToQuoteRequest(QuoteRequestDto dto) {
        return new QuoteRequest(
                dto.getDriverAge(),
                dto.getCarValue(),
                dto.getPostcode(),
                dto.getAnnualMileage(),
                dto.getClaimsInLastFiveYears(),
                dto.getCoverLevel()
        );
    }

    private QuoteResultDto.QuoteItemDto mapToItemDto(QuoteResult r) {
        QuoteResultDto.QuoteItemDto item = new QuoteResultDto.QuoteItemDto();
        item.setProviderId(r.getProviderId());
        item.setProviderName(r.getProviderName());
        item.setMonthlyPrice(r.getMonthlyPrice());
        item.setAnnualPrice(r.getAnnualPrice());
        item.setRating(r.getRating());
        item.setFeatures(r.getFeatures());
        item.setExclusions(r.getExclusions());
        item.setCoverLevel(r.getCoverLevel());
        item.setScore(r.getScore());
        item.setBestPrice(r.getIsBestPrice());
        item.setRecommended(r.getIsRecommended());
        return item;
    }
}
