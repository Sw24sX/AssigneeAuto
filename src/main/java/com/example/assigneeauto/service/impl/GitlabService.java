package com.example.assigneeauto.service.impl;

import com.example.assigneeauto.persistance.domain.Reviewer;
import com.example.assigneeauto.persistance.properties.GitlabApiProperties;
import com.example.assigneeauto.service.GitlabServiceApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gitlab4j.api.Constants;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Member;
import org.gitlab4j.api.models.MergeRequest;
import org.gitlab4j.api.models.MergeRequestFilter;
import org.gitlab4j.api.models.MergeRequestParams;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@RequiredArgsConstructor
public class GitlabService implements GitlabServiceApi {

    private final GitlabApiProperties gitlabApiProperties;
    private final GitLabApi gitLabApi;

    @Override
    @Cacheable(value = "members")
    public List<Member> getListMembers() throws GitLabApiException {
        return gitLabApi.getProjectApi().getMembers(gitlabApiProperties.getProjectId());
    }

    @Override
    public Member getMember(String username) throws GitLabApiException {
        return getListMembers().stream()
                .filter(x -> x.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    @Override
    @Cacheable(value = "merge-request-by-status-and-assignee", key = "#assigneeId")
    public List<MergeRequest> getListMergeRequestByAssigneeId(Long assigneeId, Constants.MergeRequestState status) throws GitLabApiException {
        MergeRequestFilter filter = new MergeRequestFilter();
        filter.setAssigneeId(assigneeId);
        filter.setState(status);
        filter.setProjectId(Long.parseLong(gitlabApiProperties.getProjectId()));
        return gitLabApi.getMergeRequestApi().getMergeRequests(filter);
    }

    @Override
    @Cacheable(value = "merge-request", key = "#iid")
    public Optional<MergeRequest> getMergeRequest(Long iid) {
        return gitLabApi.getMergeRequestApi().getOptionalMergeRequest(gitlabApiProperties.getProjectId(), iid);
    }


    @Override
    public boolean setAssigneeToMergeRequest(Long mergeRequestIid, Reviewer reviewer) throws GitLabApiException {
        MergeRequestParams newParams = new MergeRequestParams();
        newParams.withAssigneeId(reviewer.getMemberId());
        var result = gitLabApi.getMergeRequestApi()
                .updateMergeRequest(gitlabApiProperties.getProjectId(), mergeRequestIid, newParams);
        if (result.getAssignee() == null) {
            log.warn("For merge request {} assign reviewer {} not success", mergeRequestIid, reviewer.getUsername());
            return false;
        }
        log.info("For merge request {} assign reviewer {} success", mergeRequestIid, reviewer.getUsername());
        return true;
    }
}
