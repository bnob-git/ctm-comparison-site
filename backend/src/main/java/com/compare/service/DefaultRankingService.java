package com.compare.service;

import com.compare.config.RankingConfig;
import com.compare.domain.QuoteResult;
import org.springframework.stereotype.Service;

@Service("defaultRankingService")
public class DefaultRankingService extends AbstractRankingService {

    public DefaultRankingService(RankingConfig rankingConfig) {
        super(rankingConfig);
    }

    @Override
    protected double calculateScore(double normalizedPrice, double normalizedRating,
                                    double featureScore, QuoteResult result) {
        return rankingConfig.getPriceWeight() * normalizedPrice
                + rankingConfig.getRatingWeight() * normalizedRating
                + rankingConfig.getFeatureWeight() * featureScore;
    }
}
