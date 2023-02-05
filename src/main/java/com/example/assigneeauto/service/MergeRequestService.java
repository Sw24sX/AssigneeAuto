package com.example.assigneeauto.service;

import com.example.assigneeauto.persistance.domain.Reviewer;
import org.gitlab4j.api.models.MergeRequest;

public interface MergeRequestService {
    void setAssignee(Long mergeRequestIid, Reviewer reviewer);

    boolean setAutoAssignee(MergeRequest mergeRequest);

    /**
     * Автоматически определяет ревьювера или игнорирует его
     * @param mergeRequestIid Iid для обрабатываемого merge request
     * @return false - попытка была проигнорирована, иначе true
     */
    boolean setAutoAssigneeOrIgnore(Long mergeRequestIid);

    MergeRequest getMergeRequestGitLab(Long mergeRequestIid);
}
