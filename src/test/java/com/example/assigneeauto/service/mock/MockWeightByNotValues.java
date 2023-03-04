package com.example.assigneeauto.service.mock;

import com.example.assigneeauto.persistance.domain.Reviewer;
import com.example.assigneeauto.service.WeightByNotValuesApi;
import lombok.RequiredArgsConstructor;
import org.gitlab4j.api.models.MergeRequest;

import java.util.Map;

@RequiredArgsConstructor
public class MockWeightByNotValues implements WeightByNotValuesApi {

    private final Map<Reviewer, Long> weightByReviewer;

    @Override
    public Long getPersonalWeight(Reviewer reviewer, MergeRequest mergeRequest) {
        return weightByReviewer.get(reviewer);
    }
}
