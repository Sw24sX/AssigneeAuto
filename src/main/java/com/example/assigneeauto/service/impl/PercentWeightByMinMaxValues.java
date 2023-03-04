package com.example.assigneeauto.service.impl;

import com.example.assigneeauto.persistance.domain.Reviewer;
import com.example.assigneeauto.persistance.dto.PercentWeightByMinMaxSettings;
import com.example.assigneeauto.service.PercentWeightByMinMaxValuesApi;
import com.example.assigneeauto.service.ReviewerServiceApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
@RequiredArgsConstructor
public class PercentWeightByMinMaxValues implements PercentWeightByMinMaxValuesApi {

    private static final Integer MAX_WEIGHT_VALUE = 100;

    private final ReviewerServiceApi reviewerServiceApi;

    @Override
    public Integer getCorrectWeight(PercentWeightByMinMaxSettings settings) {
        var minCount = Long.MAX_VALUE;
        var maxCount = Long.MIN_VALUE;
        for (Reviewer activeReviewer : reviewerServiceApi.getAllActive()) {
            var count = settings.getWeightByNotValuesApi().getPersonalWeight(activeReviewer, settings.getMergeRequest());
            maxCount = Math.max(maxCount, count);
            minCount = Math.min(minCount, count);
        }

        var weight = settings.getWeightByNotValuesApi().getPersonalWeight(settings.getReviewer(), settings.getMergeRequest());
        var result = Math.toIntExact(calculateWeight(minCount, maxCount, weight));
        return settings.isRevert() ? MAX_WEIGHT_VALUE - result : result;
    }

    private Long calculateWeight(Long minCount, Long maxCount, Long current) {
        if (minCount.equals(maxCount)) {

            return 0L;
        }

        return ((current - minCount) * 100) / (maxCount - minCount);
    }
}
