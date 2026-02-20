package com.compare.service;

import com.compare.domain.QuoteResult;

import java.util.List;

public interface RankingService {
    List<QuoteResult> rank(List<QuoteResult> results);
}
