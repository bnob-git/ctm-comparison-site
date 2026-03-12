package com.compare.mapper;

import com.compare.domain.QuoteRequest;
import com.compare.domain.QuoteResult;
import com.compare.dto.QuoteRequestDto;
import com.compare.dto.QuoteResultDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuoteMapperTest {

    private final QuoteMapper mapper = new QuoteMapper();

    @Test
    void toQuoteRequest_mapsAllFields() {
        QuoteRequestDto dto = new QuoteRequestDto();
        dto.setDriverAge(30);
        dto.setCarValue(15000);
        dto.setPostcode("SW1A 1AA");
        dto.setAnnualMileage(10000);
        dto.setClaimsInLastFiveYears(1);
        dto.setCoverLevel("comprehensive");

        QuoteRequest request = mapper.toQuoteRequest(dto);

        assertEquals(30, request.getDriverAge());
        assertEquals(15000, request.getCarValue());
        assertEquals("SW1A 1AA", request.getPostcode());
        assertEquals(10000, request.getAnnualMileage());
        assertEquals(1, request.getClaimsInLastFiveYears());
        assertEquals("comprehensive", request.getCoverLevel());
    }

    @Test
    void toItemDto_mapsAllFields() {
        QuoteResult r = new QuoteResult();
        r.setProviderId(1L);
        r.setProviderName("TestProvider");
        r.setMonthlyPrice(43.75);
        r.setAnnualPrice(500.0);
        r.setRating(4.5);
        r.setFeatures("Feature1");
        r.setExclusions("Excl1");
        r.setCoverLevel("comprehensive");
        r.setScore(0.85);
        r.setIsBestPrice(true);
        r.setIsRecommended(false);
        r.setProviderLatitude(51.5);
        r.setProviderLongitude(-0.1);

        QuoteResultDto.QuoteItemDto item = mapper.toItemDto(r);

        assertEquals(1L, item.getProviderId());
        assertEquals("TestProvider", item.getProviderName());
        assertEquals(43.75, item.getMonthlyPrice());
        assertEquals(500.0, item.getAnnualPrice());
        assertEquals(4.5, item.getRating());
        assertEquals("Feature1", item.getFeatures());
        assertEquals("Excl1", item.getExclusions());
        assertEquals("comprehensive", item.getCoverLevel());
        assertEquals(0.85, item.getScore());
        assertTrue(item.isBestPrice());
        assertFalse(item.isRecommended());
        assertEquals(51.5, item.getLatitude());
        assertEquals(-0.1, item.getLongitude());
    }

    @Test
    void toItemDto_handlesNullLatLng() {
        QuoteResult r = new QuoteResult();
        r.setProviderId(2L);
        r.setProviderName("NoCoords");
        r.setProviderLatitude(null);
        r.setProviderLongitude(null);

        QuoteResultDto.QuoteItemDto item = mapper.toItemDto(r);

        assertNull(item.getLatitude());
        assertNull(item.getLongitude());
    }
}
