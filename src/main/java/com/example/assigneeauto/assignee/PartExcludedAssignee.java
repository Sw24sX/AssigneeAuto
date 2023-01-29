package com.example.assigneeauto.assignee;

import com.example.assigneeauto.persistance.domain.Reviewer;
import com.example.assigneeauto.persistance.properties.BaseAutoAssigneePartProperties;
import org.gitlab4j.api.models.MergeRequest;

/**
 * Часть алгоритма по исключению ревьюверов из исписка возможных к назначению
 */
public abstract class PartExcludedAssignee {

    private final BaseAutoAssigneePartProperties properties;
    public PartExcludedAssignee(BaseAutoAssigneePartProperties properties) {
        this.properties = properties;
    }

    /**
     * Метод, определяющий необходимость исключить ревьювера из списка возможных к назначению
     * где true - необходимо исключить из списка назначемых
     * @param reviewer ревьювер, для которого будет приниматься решение об исключении
     * @return решение об исключении ревьювера
     */
    public boolean excludeAssignee(Reviewer reviewer, MergeRequest mergeRequest) {
        if (!properties.isEnable()) {

            return false;
        }

        return getPartValue(reviewer, mergeRequest);
    }

    protected abstract boolean getPartValue(Reviewer reviewer, MergeRequest mergeRequest);
}
