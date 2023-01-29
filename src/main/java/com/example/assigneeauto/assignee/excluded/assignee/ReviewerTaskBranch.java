package com.example.assigneeauto.assignee.excluded.assignee;

import com.example.assigneeauto.assignee.PartExcludedAssignee;
import com.example.assigneeauto.persistance.domain.Reviewer;
import com.example.assigneeauto.persistance.properties.excluded.assignee.properties.ReviewTaskBranchProperties;
import com.example.assigneeauto.repository.HistoryReviewRepository;
import lombok.extern.slf4j.Slf4j;
import org.gitlab4j.api.models.MergeRequest;
import org.springframework.stereotype.Component;

/**
 * Стратегия исключения ревьюверов, если задачу из этой ветки уже проверял один из ревьюверов
 */
@Component
@Slf4j
public class ReviewerTaskBranch extends PartExcludedAssignee {


    private final HistoryReviewRepository historyReviewRepository;

    protected ReviewerTaskBranch(ReviewTaskBranchProperties reviewTaskBranchProperties,
                                 HistoryReviewRepository historyReviewRepository) {
        super(reviewTaskBranchProperties);
        this.historyReviewRepository = historyReviewRepository;
    }

    @Override
    protected boolean getPartValue(Reviewer reviewer, MergeRequest mergeRequest) {
        log.info("Run ReviewerTaskBranch for reviewer {}", reviewer.getUsername());
        String branchName = mergeRequest.getSourceBranch();
        if (!historyReviewRepository.existsByBranchName(branchName)) {
            return false;
        }

        return !historyReviewRepository.existsByBranchNameAndReviewer_Id(branchName, reviewer.getId());
    }
}
