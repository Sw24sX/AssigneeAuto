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

    /**
     * Рассчитывает вес ревьювера по переданной реализации
     * @param weightByNotValuesApi Реализация рассчета веса для каждого ревьювера
     * @param reviewer ревьвер, для которого необходимо вычислить значение
     * @param mergeRequest обрабатываемый merge request
     * @return вес ревьювера
     */
    Integer getWeightValueByReviewer(WeightByNotValuesApi weightByNotValuesApi, Reviewer reviewer, MergeRequest mergeRequest);
}
