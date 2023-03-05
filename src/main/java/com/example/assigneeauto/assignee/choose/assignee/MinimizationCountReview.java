package com.example.assigneeauto.assignee.choose.assignee;

import com.example.assigneeauto.assignee.PartChooseAssignee;
import com.example.assigneeauto.persistance.domain.Reviewer;
import com.example.assigneeauto.persistance.dto.PercentWeightByMinMaxSettings;
import com.example.assigneeauto.persistance.exception.AutoAssigneeException;
import com.example.assigneeauto.persistance.properties.choose.assignee.properties.MinimizationCountReviewProperties;
import com.example.assigneeauto.service.GitlabServiceApi;
import com.example.assigneeauto.service.PercentWeightByMinMaxValuesApi;
import com.example.assigneeauto.service.WeightByNotValuesApi;
import lombok.extern.slf4j.Slf4j;
import org.gitlab4j.api.Constants;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.MergeRequest;
import org.springframework.stereotype.Component;

/**
 * Стратегия минимизации количества назначенных merge request'ов на одного ревьювера.
 * Присваивает вес в 100 ревьюверу с наименьшим количеством merge request'ов и 0 с наибольшим
 */
@Component
@Slf4j
public class MinimizationCountReview extends PartChooseAssignee {

    private final GitlabServiceApi gitlabServiceApi;
    private final PercentWeightByMinMaxValuesApi percentWeightByMinMaxValuesApi;

    public MinimizationCountReview(MinimizationCountReviewProperties properties, GitlabServiceApi gitlabServiceApi,
                                   PercentWeightByMinMaxValuesApi percentWeightByMinMaxValuesApi) {
        super(properties);
        this.gitlabServiceApi = gitlabServiceApi;
        this.percentWeightByMinMaxValuesApi = percentWeightByMinMaxValuesApi;
    }

    @Override
    protected Integer getWeightPart(Reviewer reviewer, MergeRequest mergeRequest) {
        log.info("Run MinimizationCountReview for reviewer {}", reviewer.getUsername());
        PercentWeightByMinMaxSettings settings = PercentWeightByMinMaxSettings.builder()
                .reviewer(reviewer)
                .mergeRequest(mergeRequest)
                .weightByNotValuesApi(new CurrentWeight())
                .isRevert(true)
                .build();
        return percentWeightByMinMaxValuesApi.getCorrectWeight(settings);
    }

    private class CurrentWeight implements WeightByNotValuesApi {

        @Override
        public Long getPersonalWeight(Reviewer reviewer, MergeRequest mergeRequest) {
            try {
                return (long) gitlabServiceApi.getListMergeRequestByAssigneeId(reviewer.getMemberId(),
                        Constants.MergeRequestState.OPENED, mergeRequest.getProjectId().toString()).size();
            } catch (GitLabApiException e) {
                throw new AutoAssigneeException(e.getMessage());
            }
        }
    }
}
