package com.compare.controller;

import com.compare.controller.api.QuoteApiController;
import com.compare.domain.QuoteRequest;
import com.compare.domain.QuoteResult;
import com.compare.dto.QuoteResultDto;
import com.compare.mapper.QuoteMapper;
import com.compare.service.QuoteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(QuoteApiController.class)
class QuoteApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private QuoteService quoteService;

    @MockBean
    private QuoteMapper quoteMapper;

    @Test
    void shouldReturnQuotesForValidRequest() throws Exception {
        QuoteResult result = new QuoteResult();
        result.setProviderId(1L);
        result.setProviderName("TestProvider");
        result.setAnnualPrice(500.0);
        result.setMonthlyPrice(43.75);
        result.setRating(4.5);
        result.setFeatures("Feature1");
        result.setCoverLevel("comprehensive");
        result.setScore(0.8);

        when(quoteMapper.toQuoteRequest(any())).thenReturn(
                new QuoteRequest(30, 15000, "SW1A 1AA", 10000, 0, "comprehensive"));
        when(quoteService.getQuotes(any())).thenReturn(List.of(result));

        QuoteResultDto.QuoteItemDto itemDto = new QuoteResultDto.QuoteItemDto();
        itemDto.setProviderId(1L);
        itemDto.setProviderName("TestProvider");
        itemDto.setAnnualPrice(500.0);
        itemDto.setMonthlyPrice(43.75);
        itemDto.setRating(4.5);
        itemDto.setFeatures("Feature1");
        itemDto.setCoverLevel("comprehensive");
        itemDto.setScore(0.8);
        when(quoteMapper.toItemDto(any())).thenReturn(itemDto);

        String requestBody = """
                {
                    "driverAge": 30,
                    "carValue": 15000,
                    "postcode": "SW1A 1AA",
                    "annualMileage": 10000,
                    "claimsInLastFiveYears": 0,
                    "coverLevel": "comprehensive"
                }
                """;

        mockMvc.perform(post("/api/quotes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalResults").value(1))
                .andExpect(jsonPath("$.results[0].providerName").value("TestProvider"))
                .andExpect(jsonPath("$.results[0].annualPrice").value(500.0));
    }

    @Test
    void shouldReturnBadRequestForInvalidAge() throws Exception {
        String requestBody = """
                {
                    "driverAge": 10,
                    "carValue": 15000,
                    "postcode": "SW1A 1AA",
                    "annualMileage": 10000,
                    "claimsInLastFiveYears": 0,
                    "coverLevel": "comprehensive"
                }
                """;

        mockMvc.perform(post("/api/quotes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details.driverAge").exists());
    }

    @Test
    void shouldReturnBadRequestForMissingPostcode() throws Exception {
        String requestBody = """
                {
                    "driverAge": 30,
                    "carValue": 15000,
                    "postcode": "",
                    "annualMileage": 10000,
                    "claimsInLastFiveYears": 0,
                    "coverLevel": "comprehensive"
                }
                """;

        mockMvc.perform(post("/api/quotes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestForInvalidCoverLevel() throws Exception {
        String requestBody = """
                {
                    "driverAge": 30,
                    "carValue": 15000,
                    "postcode": "SW1A 1AA",
                    "annualMileage": 10000,
                    "claimsInLastFiveYears": 0,
                    "coverLevel": "invalid_cover"
                }
                """;

        mockMvc.perform(post("/api/quotes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }
}
