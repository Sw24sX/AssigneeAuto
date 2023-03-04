package com.example.assigneeauto.service;

import com.example.assigneeauto.persistance.domain.Reviewer;
import org.gitlab4j.api.models.MergeRequest;

/**
 * Рейализует получение необработанного веса для каждого из ревьюверов.
 * Используется для получения минимального и максимального значений и рассчета веса ревьювера на основе этого
 */
public interface WeightByNotValuesApi {
    Long getPersonalWeight(Reviewer reviewer, MergeRequest mergeRequest);
}
