package com.example.assigneeauto.service.impl;

import com.example.assigneeauto.persistance.domain.Reviewer;
import com.example.assigneeauto.persistance.dto.PercentWeightByMinMaxSettings;
import com.example.assigneeauto.service.PercentWeightByMinMaxValues;
import com.example.assigneeauto.service.ReviewerService;
import com.example.assigneeauto.service.WeightByNotValues;
import lombok.extern.slf4j.Slf4j;
import org.gitlab4j.api.models.MergeRequest;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;


@Service
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
@Slf4j
public class PercentWeightByMinMaxValuesImpl implements PercentWeightByMinMaxValues {

    private static final Integer MAX_WEIGHT_VALUE = 100;

    private final ReviewerService reviewerService;
    private final PercentWeightByMinMaxValues self;

    public PercentWeightByMinMaxValuesImpl(ReviewerService reviewerService, PercentWeightByMinMaxValues self) {
        this.reviewerService = reviewerService;
        this.self = self;
    }

    @Override
    public Integer getCorrectWeight(PercentWeightByMinMaxSettings settings) {
        int minCount = Integer.MAX_VALUE;
        int maxCount = Integer.MIN_VALUE;
        for (Reviewer activeReviewer : reviewerService.getAllActive()) {
            int count = self.getWeightValueByReviewer(settings.getWeightByNotValues(), activeReviewer,
                    settings.getMergeRequest());
            maxCount = Math.max(maxCount, count);
            minCount = Math.min(minCount, count);
        }

        Integer weight = self.getWeightValueByReviewer(settings.getWeightByNotValues(), settings.getReviewer(),
                settings.getMergeRequest());
        Integer result = calculateWeight(minCount, maxCount, weight);
        return settings.isRevert() ? MAX_WEIGHT_VALUE - result : result;
    }

    @Override
    @Cacheable(value = "calculate-weight-reviewer", keyGenerator = "percentWeightKeyGenerator")
    public Integer getWeightValueByReviewer(WeightByNotValues weightByNotValues, Reviewer reviewer, MergeRequest mergeRequest) {
        return weightByNotValues.getPersonalWeight(reviewer, mergeRequest);
    }

    private Integer calculateWeight(Integer minCount, Integer maxCount, Integer current) {
        if (minCount.equals(maxCount)) {

            return 0;
        }

        return ((current - minCount) * 100) / (maxCount - minCount);
    }
}
