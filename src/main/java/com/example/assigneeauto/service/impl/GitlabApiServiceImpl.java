package com.example.assigneeauto.service.impl;

import com.example.assigneeauto.persistance.properties.GitlabApiProperties;
import com.example.assigneeauto.service.GitlabApiService;
import lombok.extern.slf4j.Slf4j;
import org.gitlab4j.api.Constants;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.*;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class GitlabApiServiceImpl implements GitlabApiService {

    private final GitlabApiProperties gitlabApiProperties;
    private final GitLabApi gitLabApi;

    public GitlabApiServiceImpl(GitlabApiProperties gitlabApiProperties) {

        this.gitlabApiProperties = gitlabApiProperties;
        this.gitLabApi = new GitLabApi(gitlabApiProperties.getUrl(), gitlabApiProperties.getToken());
    }

    @Override
    @Cacheable(value = "members")
    public List<Member> getListMembers() throws GitLabApiException {
        return gitLabApi.getProjectApi().getMembers(gitlabApiProperties.getProjectId());
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
    public MergeRequest setAssigneeToMergeRequest(Long mergeRequestIid, Long memberId) throws GitLabApiException {
        MergeRequestParams newParams = new MergeRequestParams();
        newParams.withAssigneeId(memberId);
        var result = gitLabApi.getMergeRequestApi()
                .updateMergeRequest(gitlabApiProperties.getProjectId(), mergeRequestIid, newParams);
        log.info("For merge request {} was assigned reviewer {}", mergeRequestIid, memberId);
        return result;
    }
}
