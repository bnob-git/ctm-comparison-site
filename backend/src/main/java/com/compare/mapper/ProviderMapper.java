package com.compare.mapper;

import com.compare.domain.CoverageOption;
import com.compare.domain.Provider;
import com.compare.dto.CoverageOptionDto;
import com.compare.dto.ProviderDto;
import com.compare.dto.ProviderLocationDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProviderMapper {

    public ProviderDto toDto(Provider provider) {
        if (provider == null) {
            return null;
        }
        ProviderDto dto = new ProviderDto();
        dto.setId(provider.getId());
        dto.setName(provider.getName());
        dto.setBaseRate(provider.getBaseRate());
        dto.setRiskMultiplier(provider.getRiskMultiplier());
        dto.setFixedFee(provider.getFixedFee());
        dto.setRating(provider.getRating());
        dto.setFeatures(provider.getFeatures());
        dto.setExclusions(provider.getExclusions());
        dto.setAssumptions(provider.getAssumptions());
        dto.setLatitude(provider.getLatitude());
        dto.setLongitude(provider.getLongitude());
        if (provider.getCoverageOptions() != null) {
            dto.setCoverageOptions(provider.getCoverageOptions().stream()
                    .map(this::toCoverageOptionDto)
                    .collect(Collectors.toList()));
        }
        return dto;
    }

    public List<ProviderDto> toDtoList(List<Provider> providers) {
        if (providers == null) {
            return List.of();
        }
        return providers.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public ProviderLocationDto toLocationDto(Provider provider) {
        if (provider == null) {
            return null;
        }
        ProviderLocationDto dto = new ProviderLocationDto();
        dto.setId(provider.getId());
        dto.setName(provider.getName());
        dto.setRating(provider.getRating());
        dto.setLatitude(provider.getLatitude());
        dto.setLongitude(provider.getLongitude());
        return dto;
    }

    public List<ProviderLocationDto> toLocationDtoList(List<Provider> providers) {
        if (providers == null) {
            return List.of();
        }
        return providers.stream()
                .filter(p -> p.getLatitude() != null && p.getLongitude() != null)
                .map(this::toLocationDto)
                .collect(Collectors.toList());
    }

    private CoverageOptionDto toCoverageOptionDto(CoverageOption option) {
        if (option == null) {
            return null;
        }
        CoverageOptionDto dto = new CoverageOptionDto();
        dto.setId(option.getId());
        dto.setCoverLevel(option.getCoverLevel());
        dto.setCoverMultiplier(option.getCoverMultiplier());
        dto.setDescription(option.getDescription());
        return dto;
    }
}
