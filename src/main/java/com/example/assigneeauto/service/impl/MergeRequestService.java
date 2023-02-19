package com.example.assigneeauto.service.impl;

import com.example.assigneeauto.assignee.FullChooseAssignee;
import com.example.assigneeauto.persistance.domain.HistoryReview;
import com.example.assigneeauto.persistance.domain.Reviewer;
import com.example.assigneeauto.persistance.exception.AutoAssigneeException;
import com.example.assigneeauto.repository.HistoryReviewRepository;
import com.example.assigneeauto.service.GitlabServiceApi;
import com.example.assigneeauto.service.MergeRequestServiceApi;
import com.example.assigneeauto.service.ReviewerServiceApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gitlab4j.api.Constants;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.MergeRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class MergeRequestService implements MergeRequestServiceApi {

    private final GitlabServiceApi gitlabServiceApi;
    private final FullChooseAssignee fullChooseAssignee;
    private final HistoryReviewRepository historyReviewRepository;
    private final ReviewerServiceApi reviewerServiceApi;

    @Override
    public void setAssignee(Long mergeRequestIid, Reviewer reviewer) {

        try {
            var projectMembers = gitlabServiceApi.getListMembers();
            if (projectMembers.stream().noneMatch(member -> Objects.equals(member.getId(), reviewer.getMemberId()))) {
                throw new AutoAssigneeException("Участник c id '%s' не найден в проекте", reviewer.getMemberId().toString());
            }

            gitlabServiceApi.setAssigneeToMergeRequest(mergeRequestIid, reviewer);

        } catch (GitLabApiException e) {
            throw new AutoAssigneeException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public boolean setAutoAssignee(MergeRequest mergeRequest) {

        try {
            var reviewer = fullChooseAssignee.getAssignee(mergeRequest);
            var result = gitlabServiceApi.setAssigneeToMergeRequest(mergeRequest.getIid(), reviewer);
            updateReviewer(reviewer, mergeRequest, result);
            return result;

        } catch (GitLabApiException e) {
            log.warn(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean setAutoAssigneeOrIgnore(Long mergeRequestIid) {
        log.info("Start process merge request with iid {}", mergeRequestIid);
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

        var mergeRequest = gitlabServiceApi.getMergeRequest(mergeRequestIid)
                .orElseThrow(() -> new AutoAssigneeException("Merge request c id '%s' не найден в проекте",
                        mergeRequestIid.toString()));
        if (!mergeRequest.getState().equals(Constants.MergeRequestState.OPENED.toValue())) {
            throw new AutoAssigneeException("Открытый merge request c id '%s' не найден в проекте", mergeRequestIid.toString());
        }
        
        return mergeRequest;
    }

    private void updateReviewer(Reviewer reviewer, MergeRequest mergeRequest, boolean result) {
        var taskBranch = mergeRequest.getSourceBranch();
        if (!historyReviewRepository.existsByMergeRequestIid(mergeRequest.getIid())) {
            var historyReview = new HistoryReview();
            historyReview.setReviewer(reviewer);
            historyReview.setBranchName(taskBranch);
            historyReview.setMergeRequestIid(mergeRequest.getIid());
            historyReview.setMergeRequestName(mergeRequest.getTitle());
            historyReview.setSuccess(result);
            historyReview = historyReviewRepository.save(historyReview);
//            reviewer.getHistoryReviews().add(historyReview);
        }

//        reviewerServiceApi.saveReviewer(reviewer);
    }
}
