package com.compare.dto;

import jakarta.validation.constraints.*;

public record QuoteRequestDto(
        @Min(value = 17, message = "Driver must be at least 17 years old")
        @Max(value = 99, message = "Driver age must be 99 or below")
        int driverAge,

        @Min(value = 500, message = "Car value must be at least £500")
        @Max(value = 200000, message = "Car value must be £200,000 or below")
        double carValue,

        @NotBlank(message = "Postcode is required")
        @Pattern(regexp = "^[A-Za-z]{1,2}\\d[A-Za-z\\d]?\\s?\\d[A-Za-z]{2}$",
                 message = "Please enter a valid UK postcode")
        String postcode,

        @Min(value = 0, message = "Annual mileage cannot be negative")
        @Max(value = 100000, message = "Annual mileage must be 100,000 or below")
        int annualMileage,

        @Min(value = 0, message = "Claims cannot be negative")
        @Max(value = 10, message = "Claims must be 10 or fewer")
        int claimsInLastFiveYears,

        @NotBlank(message = "Cover level is required")
        @Pattern(regexp = "^(third_party|tp_fire_theft|comprehensive)$",
                 message = "Cover level must be third_party, tp_fire_theft, or comprehensive")
        String coverLevel
) {}
