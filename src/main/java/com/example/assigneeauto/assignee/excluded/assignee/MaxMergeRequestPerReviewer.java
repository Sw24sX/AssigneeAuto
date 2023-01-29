package com.example.assigneeauto.assignee.excluded.assignee;

import com.example.assigneeauto.assignee.PartExcludedAssignee;
import com.example.assigneeauto.persistance.domain.Reviewer;
import com.example.assigneeauto.persistance.exception.AutoAssigneeException;
import com.example.assigneeauto.persistance.properties.excluded.assignee.properties.MaxMergeRequestPerReviewerProperties;
import com.example.assigneeauto.service.GitlabApiService;
import lombok.extern.slf4j.Slf4j;
import org.gitlab4j.api.Constants;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.MergeRequest;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Исключает ревьюверов, на которых уже назначено достаточно merge request'ов на текущий момент
 */
@Component
@Slf4j
public class MaxMergeRequestPerReviewer extends PartExcludedAssignee {

    private final GitlabApiService gitlabApiService;
    private final MaxMergeRequestPerReviewerProperties properties;

    public MaxMergeRequestPerReviewer(GitlabApiService gitlabApiService,
                                      MaxMergeRequestPerReviewerProperties properties) {
        super(properties);
        this.gitlabApiService = gitlabApiService;
        this.properties = properties;
    }

    @Override
    protected boolean getPartValue(Reviewer reviewer, MergeRequest mergeRequest) {
        log.info("Run MaxMergeRequestPerReviewer for reviewer {}", reviewer.getUsername());
        try {
            List<MergeRequest> mergeRequests = gitlabApiService
                    .getListMergeRequestByAssigneeId(reviewer.getMemberId(), Constants.MergeRequestState.OPENED);

            return mergeRequests.size() >= properties.getMaxMergeRequests();
        } catch (GitLabApiException e) {
            throw new AutoAssigneeException(e.getMessage());
        }
    }
}
