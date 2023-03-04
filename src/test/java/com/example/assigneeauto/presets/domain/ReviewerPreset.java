package com.example.assigneeauto.presets.domain;

import com.example.assigneeauto.persistance.domain.Reviewer;
import com.example.assigneeauto.persistance.domain.ReviewerInfo;
import com.example.assigneeauto.presets.test.dto.ReviewerData;
import com.example.assigneeauto.presets.test.ReviewerDataPreset;
import org.gitlab4j.api.models.AccessLevel;

public class ReviewerPreset {
    public static Reviewer first() {
        var reviewerData = ReviewerDataPreset.first();
        return createByReviewerData(reviewerData);
    }

    public static Reviewer second() {
        var reviewerData = ReviewerDataPreset.second();
        return createByReviewerData(reviewerData);
    }

    public static Reviewer third() {
        var reviewerData = ReviewerDataPreset.third();
        return createByReviewerData(reviewerData);
    }

    public static Reviewer fourth() {
        var reviewerData = ReviewerDataPreset.fourth();
        return createByReviewerData(reviewerData);
    }

    public static Reviewer fifth() {
        var reviewerData = ReviewerDataPreset.fifth();
        return createByReviewerData(reviewerData);
    }

    public static Reviewer inactive() {
        var reviewerData = ReviewerDataPreset.inactive();
        return createByReviewerData(reviewerData);
    }

    private static Reviewer createByReviewerData(ReviewerData reviewerData) {
        var reviewer = new Reviewer();
        reviewer.setId(Long.parseLong(reviewerData.getId()));
        reviewer.setUsername(reviewerData.getUsername());
        reviewer.setReviewerNames(reviewerData.getName());
        reviewer.setMemberId(Long.parseLong(reviewerData.getId()));
        reviewer.setAccessLevelGitLab(AccessLevel.MAINTAINER);
        reviewer.setReviewAccess(reviewerData.isActive());

        var info = new ReviewerInfo();
        info.setReviewer(reviewer);
        info.setMaxCountReview(reviewerData.getMaxCountReview());
        info.setId(Long.parseLong(reviewerData.getId()));
        reviewer.setInfo(info);

        return reviewer;
    }
}
