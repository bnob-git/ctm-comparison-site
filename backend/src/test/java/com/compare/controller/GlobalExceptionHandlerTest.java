package com.compare.controller;

import com.compare.controller.api.QuoteApiController;
import com.compare.mapper.QuoteMapper;
import com.compare.service.QuoteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(QuoteApiController.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QuoteService quoteService;

    @MockBean
    private QuoteMapper quoteMapper;

    @Test
    void validationError_returns400WithConsistentShape() throws Exception {
        String invalidRequest = """
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
                        .content(invalidRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation failed"))
                .andExpect(jsonPath("$.details").exists())
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void multipleValidationErrors_returns400() throws Exception {
        String invalidRequest = """
                {
                    "driverAge": 10,
                    "carValue": -1,
                    "postcode": "",
                    "annualMileage": 10000,
                    "claimsInLastFiveYears": 0,
                    "coverLevel": "invalid"
                }
                """;

        mockMvc.perform(post("/api/quotes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation failed"))
                .andExpect(jsonPath("$.details").isNotEmpty());
    }
}
