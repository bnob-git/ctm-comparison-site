package com.compare.controller.api;

import com.compare.domain.QuoteResult;
import com.compare.dto.QuoteRequestDto;
import com.compare.dto.QuoteResultDto;
import com.compare.mapper.QuoteMapper;
import com.compare.service.QuoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@Tag(name = "Quotes", description = "Quote comparison endpoints")
public class QuoteApiController {

    private static final Logger log = LoggerFactory.getLogger(QuoteApiController.class);

    private final QuoteService quoteService;
    private final QuoteMapper quoteMapper;

    public QuoteApiController(QuoteService quoteService, QuoteMapper quoteMapper) {
        this.quoteService = quoteService;
        this.quoteMapper = quoteMapper;
    }

    @PostMapping("/quotes")
    @Operation(summary = "Get insurance quotes", description = "Submit quote request and receive ranked provider quotes")
    public ResponseEntity<QuoteResultDto> getQuotes(@Valid @RequestBody QuoteRequestDto dto) {
        List<QuoteResult> results = quoteService.getQuotes(quoteMapper.toQuoteRequest(dto));

        QuoteResultDto response = new QuoteResultDto();
        response.setQuoteId(UUID.randomUUID().toString().substring(0, 8));
        response.setTotalResults(results.size());
        response.setSortedBy("score");
        response.setResults(results.stream().map(quoteMapper::toItemDto).collect(Collectors.toList()));

        return ResponseEntity.ok(response);
    }
}
