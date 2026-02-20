package com.compare.seed;

import com.compare.domain.CoverageOption;
import com.compare.domain.Provider;
import com.compare.repository.ProviderRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final ProviderRepository providerRepository;
    private final ObjectMapper objectMapper;

    public DataSeeder(ProviderRepository providerRepository, ObjectMapper objectMapper) {
        this.providerRepository = providerRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void run(String... args) throws Exception {
        if (providerRepository.count() > 0) {
            log.info("Providers already seeded, skipping");
            return;
        }

        log.info("Seeding provider data from providers.json");

        InputStream is = new ClassPathResource("data/providers.json").getInputStream();
        List<Map<String, Object>> providerData = objectMapper.readValue(is, new TypeReference<>() {});

        for (Map<String, Object> data : providerData) {
            Provider provider = new Provider();
            provider.setName((String) data.get("name"));
            provider.setBaseRate(((Number) data.get("baseRate")).doubleValue());
            provider.setRiskMultiplier(((Number) data.get("riskMultiplier")).doubleValue());
            provider.setFixedFee(((Number) data.get("fixedFee")).doubleValue());
            provider.setRating(((Number) data.get("rating")).doubleValue());
            provider.setFeatures((String) data.get("features"));
            provider.setExclusions((String) data.get("exclusions"));
            provider.setAssumptions((String) data.get("assumptions"));

            List<Map<String, Object>> options = (List<Map<String, Object>>) data.get("coverageOptions");
            if (options != null) {
                for (Map<String, Object> opt : options) {
                    CoverageOption coverageOption = new CoverageOption();
                    coverageOption.setCoverLevel((String) opt.get("coverLevel"));
                    coverageOption.setCoverMultiplier(((Number) opt.get("coverMultiplier")).doubleValue());
                    coverageOption.setDescription((String) opt.get("description"));
                    provider.addCoverageOption(coverageOption);
                }
            }

            providerRepository.save(provider);
            log.info("Seeded provider: {} (baseRate={}, rating={})",
                    provider.getName(), provider.getBaseRate(), provider.getRating());
        }

        log.info("Seeded {} providers successfully", providerRepository.count());
    }
}
