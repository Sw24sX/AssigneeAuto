package com.example.assigneeauto.presets.domain;

import com.example.assigneeauto.persistance.domain.HistoryReview;
import com.example.assigneeauto.persistance.domain.Reviewer;
import com.example.assigneeauto.presets.test.HistoryReviewDataPreset;
import com.example.assigneeauto.presets.test.dto.HistoryReviewData;

public class HistoryReviewPreset {

    public static HistoryReview first() {
        var data = HistoryReviewDataPreset.first();
        var reviewer = ReviewerPreset.first();
        return createHistoryReview(data, reviewer);
    }

    public static HistoryReview second() {
        var data = HistoryReviewDataPreset.second();
        var reviewer = ReviewerPreset.second();
        return createHistoryReview(data, reviewer);
    }

    public static HistoryReview wrong() {
        var data = HistoryReviewDataPreset.wrong();
        var reviewer = ReviewerPreset.first();
        return createHistoryReview(data, reviewer);
    }

    public static HistoryReview reviewerInactiveSuccess() {
        var data = HistoryReviewDataPreset.first();
        var reviewer = ReviewerPreset.inactive();
        return createHistoryReview(data, reviewer);
    }

    public static HistoryReview reviewerInactiveWrong() {
        var data = HistoryReviewDataPreset.wrong();
        var reviewer = ReviewerPreset.inactive();
        return createHistoryReview(data, reviewer);
    }

    private static HistoryReview createHistoryReview(HistoryReviewData reviewData, Reviewer reviewer) {
        var result = new HistoryReview();
        result.setReviewer(reviewer);

        result.setSuccess(reviewData.isSuccess());
        result.setMergeRequestName(reviewData.getMergeRequestName());
        result.setBranchName(reviewData.getBranchName());
        result.setId(reviewData.getId());
        result.setMergeRequestIid(reviewData.getMergeRequestIid());
        return result;
    }
}
