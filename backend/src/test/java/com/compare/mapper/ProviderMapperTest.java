package com.compare.mapper;

import com.compare.domain.CoverageOption;
import com.compare.domain.Provider;
import com.compare.dto.CoverageOptionDto;
import com.compare.dto.ProviderDto;
import com.compare.dto.ProviderLocationDto;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProviderMapperTest {

    private final ProviderMapper mapper = new ProviderMapper();

    private Provider createProvider(Long id, String name, Double lat, Double lng) {
        Provider p = new Provider();
        p.setId(id);
        p.setName(name);
        p.setBaseRate(300.0);
        p.setRiskMultiplier(1.0);
        p.setFixedFee(40.0);
        p.setRating(4.5);
        p.setFeatures("Feature1,Feature2");
        p.setExclusions("Excl1");
        p.setAssumptions("Assume1");
        p.setLatitude(lat);
        p.setLongitude(lng);
        return p;
    }

    @Test
    void toDto_mapsAllFields() {
        Provider p = createProvider(1L, "TestProvider", 51.5, -0.1);

        CoverageOption co = new CoverageOption();
        co.setId(10L);
        co.setCoverLevel("comprehensive");
        co.setCoverMultiplier(1.5);
        co.setDescription("Full cover");
        p.addCoverageOption(co);

        ProviderDto dto = mapper.toDto(p);

        assertEquals(1L, dto.getId());
        assertEquals("TestProvider", dto.getName());
        assertEquals(300.0, dto.getBaseRate());
        assertEquals(1.0, dto.getRiskMultiplier());
        assertEquals(40.0, dto.getFixedFee());
        assertEquals(4.5, dto.getRating());
        assertEquals("Feature1,Feature2", dto.getFeatures());
        assertEquals("Excl1", dto.getExclusions());
        assertEquals("Assume1", dto.getAssumptions());
        assertEquals(51.5, dto.getLatitude());
        assertEquals(-0.1, dto.getLongitude());
        assertEquals(1, dto.getCoverageOptions().size());

        CoverageOptionDto coDto = dto.getCoverageOptions().get(0);
        assertEquals(10L, coDto.getId());
        assertEquals("comprehensive", coDto.getCoverLevel());
        assertEquals(1.5, coDto.getCoverMultiplier());
        assertEquals("Full cover", coDto.getDescription());
    }

    @Test
    void toDto_nullSafe() {
        Provider p = new Provider();
        p.setId(1L);
        p.setName("Minimal");

        ProviderDto dto = mapper.toDto(p);

        assertEquals(1L, dto.getId());
        assertEquals("Minimal", dto.getName());
        assertNull(dto.getFeatures());
        assertNull(dto.getLatitude());
        assertNull(dto.getLongitude());
        assertNotNull(dto.getCoverageOptions());
        assertTrue(dto.getCoverageOptions().isEmpty());
    }

    @Test
    void toDtoList_convertsAll() {
        List<Provider> providers = List.of(
                createProvider(1L, "A", 51.0, -0.1),
                createProvider(2L, "B", 52.0, -1.0));

        List<ProviderDto> dtos = mapper.toDtoList(providers);

        assertEquals(2, dtos.size());
        assertEquals("A", dtos.get(0).getName());
        assertEquals("B", dtos.get(1).getName());
    }

    @Test
    void toLocationDtoList_filtersNullLatLng() {
        List<Provider> providers = List.of(
                createProvider(1L, "WithCoords", 51.0, -0.1),
                createProvider(2L, "NoLat", null, -0.1),
                createProvider(3L, "NoLng", 51.0, null),
                createProvider(4L, "NoBoth", null, null));

        List<ProviderLocationDto> locations = mapper.toLocationDtoList(providers);

        assertEquals(1, locations.size());
        assertEquals("WithCoords", locations.get(0).getName());
        assertEquals(51.0, locations.get(0).getLatitude());
        assertEquals(-0.1, locations.get(0).getLongitude());
    }

    @Test
    void toLocationDto_mapsFields() {
        Provider p = createProvider(5L, "MapProvider", 53.5, -2.2);
        p.setRating(3.8);

        ProviderLocationDto dto = mapper.toLocationDto(p);

        assertEquals(5L, dto.getId());
        assertEquals("MapProvider", dto.getName());
        assertEquals(3.8, dto.getRating());
        assertEquals(53.5, dto.getLatitude());
        assertEquals(-2.2, dto.getLongitude());
    }
}
