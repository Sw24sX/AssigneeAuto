package com.example.assigneeauto.service.impl;

import com.example.assigneeauto.persistance.domain.Reviewer;
import com.example.assigneeauto.persistance.dto.PercentWeightByMinMaxSettings;
import com.example.assigneeauto.service.PercentWeightByMinMaxValuesApi;
import com.example.assigneeauto.service.ReviewerServiceApi;
import com.example.assigneeauto.service.WeightByNotValuesApi;
import lombok.extern.slf4j.Slf4j;
import org.gitlab4j.api.models.MergeRequest;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class PercentWeightByMinMaxValues implements PercentWeightByMinMaxValuesApi {

    private static final Integer MAX_WEIGHT_VALUE = 100;

    private final ReviewerServiceApi reviewerServiceApi;
    private final PercentWeightByMinMaxValuesApi self;

    public PercentWeightByMinMaxValues(ReviewerServiceApi reviewerServiceApi, PercentWeightByMinMaxValuesApi self) {
        this.reviewerServiceApi = reviewerServiceApi;
        this.self = self;
    }

    @Override
    public Integer getCorrectWeight(PercentWeightByMinMaxSettings settings) {
        int minCount = Integer.MAX_VALUE;
        int maxCount = Integer.MIN_VALUE;
        for (Reviewer activeReviewer : reviewerServiceApi.getAllActive()) {
            int count = self.getWeightValueByReviewer(settings.getWeightByNotValuesApi(), activeReviewer,
                    settings.getMergeRequest());
            maxCount = Math.max(maxCount, count);
            minCount = Math.min(minCount, count);
        }

        Integer weight = self.getWeightValueByReviewer(settings.getWeightByNotValuesApi(), settings.getReviewer(),
                settings.getMergeRequest());
        Integer result = calculateWeight(minCount, maxCount, weight);
        return settings.isRevert() ? MAX_WEIGHT_VALUE - result : result;
    }

    @Override
    @Cacheable(value = "calculate-weight-reviewer", keyGenerator = "percentWeightKeyGenerator")
    public Integer getWeightValueByReviewer(WeightByNotValuesApi weightByNotValuesApi, Reviewer reviewer, MergeRequest mergeRequest) {
        return weightByNotValuesApi.getPersonalWeight(reviewer, mergeRequest);
    }

    private Integer calculateWeight(Integer minCount, Integer maxCount, Integer current) {
        if (minCount.equals(maxCount)) {

            return 0;
        }

        return ((current - minCount) * 100) / (maxCount - minCount);
    }
}
