package com.example.assigneeauto.repository;

import com.example.assigneeauto.persistance.domain.HistoryReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HistoryReviewRepository  extends JpaRepository<HistoryReview, Long> {
    boolean existsByBranchNameAndReviewer_Id(String branchName, Long reviewer_id);

    boolean existsByBranchName(String branchName);

    HistoryReview findByMergeRequestIid(Long mergeRequestIid);
}
