package com.compare.service;

import com.compare.domain.Provider;
import com.compare.domain.QuoteRequest;
import com.compare.domain.QuoteResult;

import java.util.List;

public interface PricingEngine {
    List<QuoteResult> calculateQuotes(QuoteRequest request, List<Provider> providers);
}
