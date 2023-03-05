package com.example.assigneeauto.service;

import com.example.assigneeauto.persistance.domain.Reviewer;
import org.gitlab4j.api.models.MergeRequest;

public interface MergeRequestServiceApi {
    void setAssignee(Long mergeRequestIid, Reviewer reviewer, String projectId);

    boolean setAutoAssignee(MergeRequest mergeRequest);

    /**
     * Автоматически определяет ревьювера или игнорирует его
     * @param mergeRequestIid Iid для обрабатываемого merge request
     * @return false - попытка была проигнорирована, иначе true
     */
    boolean setAutoAssigneeOrIgnore(Long mergeRequestIid, String projectId);

    MergeRequest getMergeRequestGitLab(Long mergeRequestIid, String projectId);
}
