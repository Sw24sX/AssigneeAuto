package com.example.assigneeauto.persistance.dto;

import com.example.assigneeauto.persistance.domain.Reviewer;
import com.example.assigneeauto.service.WeightByNotValues;
import lombok.Builder;
import lombok.Data;
import org.gitlab4j.api.models.MergeRequest;

@Data
@Builder
public class PercentWeightByMinMaxSettings {
    /**
     * ревьвер, для которого необходимо вычислить итоговое значение на основании минимального и максимального значений
     */
    private final Reviewer reviewer;

    /**
     * обрабатываемый merge request
     */
    private final MergeRequest mergeRequest;

    /**
     * Реализация рассчета веса для каждого ревьювера
     */
    private final WeightByNotValues weightByNotValues;

    /**
     * Инвертировать результат
     */
    private boolean isRevert;
}
