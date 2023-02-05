package com.example.assigneeauto.service.impl;

import com.example.assigneeauto.assignee.FullChooseAssignee;
import com.example.assigneeauto.persistance.domain.HistoryReview;
import com.example.assigneeauto.persistance.domain.Reviewer;
import com.example.assigneeauto.persistance.exception.AutoAssigneeException;
import com.example.assigneeauto.repository.HistoryReviewRepository;
import com.example.assigneeauto.service.GitlabApiService;
import com.example.assigneeauto.service.MergeRequestService;
import com.example.assigneeauto.service.ReviewerService;
import lombok.extern.slf4j.Slf4j;
import org.gitlab4j.api.Constants;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.MergeRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Slf4j
public class MergeRequestServiceImpl implements MergeRequestService {

    private final GitlabApiService gitlabApiService;
    private final FullChooseAssignee fullChooseAssignee;
    private final HistoryReviewRepository historyReviewRepository;
    private final ReviewerService reviewerService;

    public MergeRequestServiceImpl(GitlabApiService gitlabApiService, FullChooseAssignee fullChooseAssignee,
                                   HistoryReviewRepository historyReviewRepository, ReviewerService reviewerService) {
        this.gitlabApiService = gitlabApiService;
        this.fullChooseAssignee = fullChooseAssignee;
        this.historyReviewRepository = historyReviewRepository;
        this.reviewerService = reviewerService;
    }

    @Override
    public MergeRequest setAssignee(Long mergeRequestIid, Long assigneeId) {

        try {
            var projectMembers = gitlabApiService.getListMembers();
            if (projectMembers.stream().noneMatch(member -> Objects.equals(member.getId(), assigneeId))) {
                throw new AutoAssigneeException("Участник c id '%s' не найден в проекте", assigneeId.toString());
            }

            return gitlabApiService.setAssigneeToMergeRequest(mergeRequestIid, assigneeId);

        } catch (GitLabApiException e) {
            throw new AutoAssigneeException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public MergeRequest setAutoAssignee(MergeRequest mergeRequest) {

        try {
            var reviewer = fullChooseAssignee.getAssignee(mergeRequest);
            mergeRequest = gitlabApiService.setAssigneeToMergeRequest(mergeRequest.getIid(), reviewer.getMemberId());
            updateReviewer(reviewer, mergeRequest);

            return mergeRequest;

        } catch (GitLabApiException e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    @Override
    public boolean setAutoAssigneeOrIgnore(Long mergeRequestIid) {
        var historyReview = historyReviewRepository.findByMergeRequestIid(mergeRequestIid);
        if (historyReview != null) {
            return false;
        }

        var mergeRequest = getMergeRequestGitLab(mergeRequestIid);
        if (mergeRequest.getAssignee() != null) {
            return false;
        }

        setAutoAssignee(mergeRequest);
        return true;
    }

    @Override
    public MergeRequest getMergeRequestGitLab(Long mergeRequestIid) {

        var mergeRequest = gitlabApiService.getMergeRequest(mergeRequestIid)
                .orElseThrow(() -> new AutoAssigneeException("Merge request c id '%s' не найден в проекте",
                        mergeRequestIid.toString()));
        if (!mergeRequest.getState().equals(Constants.MergeRequestState.OPENED.toValue())) {
            throw new AutoAssigneeException("Открытый merge request c id '%s' не найден в проекте", mergeRequestIid.toString());
        }
        
        return mergeRequest;
    }

    private void updateReviewer(Reviewer reviewer, MergeRequest mergeRequest) {
        var taskBranch = mergeRequest.getSourceBranch();
        // TODO: 04.02.2023 Сделать проверку по Iid
        if (!historyReviewRepository.existsByBranchNameAndReviewer_Id(taskBranch, reviewer.getId())) {
            HistoryReview historyReview = new HistoryReview();
            historyReview.setReviewer(reviewer);
            historyReview.setBranchName(taskBranch);
            historyReview.setMergeRequestIid(mergeRequest.getIid());
            historyReview = historyReviewRepository.save(historyReview);
            reviewer.getHistoryReviews().add(historyReview);
        }

        reviewerService.updateReviewer(reviewer);
    }
}
