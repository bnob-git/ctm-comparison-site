package com.compare.mapper;

import com.compare.domain.QuoteRequest;
import com.compare.domain.QuoteResult;
import com.compare.dto.QuoteRequestDto;
import com.compare.dto.QuoteResultDto;
import org.springframework.stereotype.Component;

@Component
public class QuoteMapper {

    public QuoteRequest toQuoteRequest(QuoteRequestDto dto) {
        if (dto == null) {
            return null;
        }
        return new QuoteRequest(
                dto.getDriverAge(),
                dto.getCarValue(),
                dto.getPostcode(),
                dto.getAnnualMileage(),
                dto.getClaimsInLastFiveYears(),
                dto.getCoverLevel()
        );
    }

    public QuoteResultDto.QuoteItemDto toItemDto(QuoteResult result) {
        if (result == null) {
            return null;
        }
        QuoteResultDto.QuoteItemDto item = new QuoteResultDto.QuoteItemDto();
        item.setProviderId(result.getProviderId());
        item.setProviderName(result.getProviderName());
        item.setMonthlyPrice(result.getMonthlyPrice());
        item.setAnnualPrice(result.getAnnualPrice());
        item.setRating(result.getRating());
        item.setFeatures(result.getFeatures());
        item.setExclusions(result.getExclusions());
        item.setCoverLevel(result.getCoverLevel());
        item.setScore(result.getScore());
        item.setBestPrice(result.getIsBestPrice());
        item.setRecommended(result.getIsRecommended());
        item.setLatitude(result.getProviderLatitude());
        item.setLongitude(result.getProviderLongitude());
        return item;
    }
}
