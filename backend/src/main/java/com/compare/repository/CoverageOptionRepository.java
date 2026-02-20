package com.compare.repository;

import com.compare.domain.CoverageOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoverageOptionRepository extends JpaRepository<CoverageOption, Long> {
    List<CoverageOption> findByProviderId(Long providerId);
}
