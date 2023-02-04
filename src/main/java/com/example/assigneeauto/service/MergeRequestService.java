package com.example.assigneeauto.service;

import org.gitlab4j.api.models.MergeRequest;

public interface MergeRequestService {
    MergeRequest setAssignee(Long mergeRequestIid, Long assigneeId);

    MergeRequest setAutoAssignee(MergeRequest mergeRequest);

    /**
     * Автоматически определяет ревьювера или игнорирует его
     * @param mergeRequestIid Iid для обрабатываемого merge request
     * @return false - попытка была проигнорирована, иначе true
     */
    boolean setAutoAssigneeOrIgnore(Long mergeRequestIid);

    MergeRequest getMergeRequestGitLab(Long mergeRequestIid);
}
