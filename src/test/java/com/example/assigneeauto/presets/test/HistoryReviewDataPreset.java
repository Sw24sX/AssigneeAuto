package com.example.assigneeauto.presets.test;

import com.example.assigneeauto.presets.test.dto.HistoryReviewData;

public class HistoryReviewDataPreset {
    public static HistoryReviewData first() {
        return HistoryReviewData.builder()
                .id(1L)
                .mergeRequestIid(1L)
                .branchName(GitBranch.TEST_1.getName())
                .isSuccess(true)
                .mergeRequestName("test merge request 1")
                .build();
    }

    public static HistoryReviewData second() {
        return HistoryReviewData.builder()
                .id(2L)
                .mergeRequestIid(2L)
                .branchName(GitBranch.TEST_2.getName())
                .isSuccess(true)
                .mergeRequestName("test merge request 2")
                .build();
    }

    public static HistoryReviewData wrong() {
        return HistoryReviewData.builder()
                .id(3L)
                .mergeRequestIid(3L)
                .branchName(GitBranch.TEST_1.getName())
                .isSuccess(false)
                .mergeRequestName("test merge request 1")
                .build();
    }
}
