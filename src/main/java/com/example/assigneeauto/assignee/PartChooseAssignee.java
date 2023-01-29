package com.example.assigneeauto.assignee;

import com.example.assigneeauto.persistance.domain.Reviewer;
import com.example.assigneeauto.persistance.properties.choose.assignee.properties.BaseChooseAssigneeProperties;
import org.gitlab4j.api.models.MergeRequest;

/**
 * Часть алгоритма выбора ревьювера
 */
public abstract class PartChooseAssignee {

    private final BaseChooseAssigneeProperties baseChooseAssigneeProperties;

    protected PartChooseAssignee(BaseChooseAssigneeProperties baseChooseAssigneeProperties) {
        this.baseChooseAssigneeProperties = baseChooseAssigneeProperties;
    }

    /**
     * Получить вес для переданного ревьювера
     * Вес - целое число в промежутке от 0 до 100,
     * где 0 - ревьювер совершенно не подходит для ревью из рассчета данной части алгоритма, а 100 - абсолютно подходит
     * @param reviewer ревьювер, для которого будет рассчитываться вес
     * @return вес в промежутке от 0 до 100
     */
    public Integer getWeight(Reviewer reviewer, MergeRequest mergeRequest) {
        if (!baseChooseAssigneeProperties.isEnable() || baseChooseAssigneeProperties.getCoefficient() == 0) {
            return 0;
        }
        double result = getWeightPart(reviewer, mergeRequest) * baseChooseAssigneeProperties.getCoefficient();

        return (int) result;
    }

    protected abstract Integer getWeightPart(Reviewer reviewer, MergeRequest mergeRequest);
}
