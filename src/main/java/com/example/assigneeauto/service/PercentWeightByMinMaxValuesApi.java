package com.example.assigneeauto.service;

import com.example.assigneeauto.persistance.domain.Reviewer;
import com.example.assigneeauto.persistance.dto.PercentWeightByMinMaxSettings;
import org.gitlab4j.api.models.MergeRequest;

/**
 * Получение веса от 0 до 100 на основании рассчета минимального и максимального значения.
 */
public interface PercentWeightByMinMaxValuesApi {

    /**
     * Рассчитывает вес ревьювера на основании минимального и максимального значения всех ревьюверов
     * @param settings параметры для рассчета веса ревьювера
     * @return вес ревьювера
     */
    Integer getCorrectWeight(PercentWeightByMinMaxSettings settings);
}
