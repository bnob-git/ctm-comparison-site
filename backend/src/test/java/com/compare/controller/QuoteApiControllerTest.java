package com.compare.controller;

import com.compare.controller.api.QuoteApiController;
import com.compare.domain.QuoteResult;
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

    @Test
    void shouldReturnQuotesForValidRequest() throws Exception {
        QuoteResult result = new QuoteResult(
                1L, "TestProvider", 43.75, 500.0, 4.5,
                "Feature1", null, "comprehensive", 0.8, false, false
        );

        when(quoteService.getQuotes(any())).thenReturn(List.of(result));

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
                .andExpect(jsonPath("$.errors.driverAge").exists());
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
