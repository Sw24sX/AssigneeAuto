package com.example.assigneeauto.presets.test.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class HistoryReviewData {
    private String branchName;
    private Long mergeRequestIid;
    private String mergeRequestName;
    private boolean isSuccess;
    private Long id;
}
