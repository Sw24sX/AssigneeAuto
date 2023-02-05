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
    public void setAssignee(Long mergeRequestIid, Reviewer reviewer) {

        try {
            var projectMembers = gitlabApiService.getListMembers();
            if (projectMembers.stream().noneMatch(member -> Objects.equals(member.getId(), reviewer.getMemberId()))) {
                throw new AutoAssigneeException("Участник c id '%s' не найден в проекте", reviewer.getMemberId().toString());
            }

            gitlabApiService.setAssigneeToMergeRequest(mergeRequestIid, reviewer);

        } catch (GitLabApiException e) {
            throw new AutoAssigneeException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public boolean setAutoAssignee(MergeRequest mergeRequest) {

        try {
            var reviewer = fullChooseAssignee.getAssignee(mergeRequest);
            var result = gitlabApiService.setAssigneeToMergeRequest(mergeRequest.getIid(), reviewer);
            if (result) {
                updateReviewer(reviewer, mergeRequest);
            }

            return result;

        } catch (GitLabApiException e) {
            log.warn(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean setAutoAssigneeOrIgnore(Long mergeRequestIid) {
        var historyReview = historyReviewRepository.findByMergeRequestIid(mergeRequestIid);
        if (historyReview != null) {
            log.warn("Merge request {} was ignored, because it exist in history_review table with id {}",
                    mergeRequestIid, historyReview.getId());
            return false;
        }

        var mergeRequest = getMergeRequestGitLab(mergeRequestIid);
        if (mergeRequest.getAssignee() != null) {
            log.warn("Merge request {} was ignored, because assignee already exists", mergeRequestIid);
            return false;
        }

        return setAutoAssignee(mergeRequest);
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
        if (!historyReviewRepository.existsByMergeRequestIid(mergeRequest.getIid())) {
            var historyReview = new HistoryReview();
            historyReview.setReviewer(reviewer);
            historyReview.setBranchName(taskBranch);
            historyReview.setMergeRequestIid(mergeRequest.getIid());
            historyReview = historyReviewRepository.save(historyReview);
            reviewer.getHistoryReviews().add(historyReview);
        }

        reviewerService.updateReviewer(reviewer);
    }
}
