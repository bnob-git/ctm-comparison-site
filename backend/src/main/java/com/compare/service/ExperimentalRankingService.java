package com.compare.service;

import com.compare.config.RankingConfig;
import com.compare.domain.QuoteResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("experimentalRankingService")
public class ExperimentalRankingService extends AbstractRankingService {

    private static final Logger log = LoggerFactory.getLogger(ExperimentalRankingService.class);

    public ExperimentalRankingService(RankingConfig rankingConfig) {
        super(rankingConfig);
    }

    @Override
    public List<QuoteResult> rank(List<QuoteResult> results) {
        log.info("Using EXPERIMENTAL ranking algorithm");
        return super.rank(results);
    }

    @Override
    protected double calculateScore(double normalizedPrice, double normalizedRating,
                                    double featureScore, QuoteResult result) {
        // Value-for-money: rating per pound
        double valueScore = result.getRating() / (result.getAnnualPrice() / 1000.0);
        double normalizedValue = Math.min(valueScore / 10.0, 1.0);

        return 0.3 * normalizedPrice
                + 0.35 * normalizedRating
                + 0.15 * featureScore
                + 0.2 * normalizedValue;
    }
}
