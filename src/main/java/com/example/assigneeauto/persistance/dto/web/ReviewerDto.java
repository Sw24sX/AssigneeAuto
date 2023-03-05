package com.example.assigneeauto.persistance.dto.web;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ReviewerDto {
    private Long id;
    private String username;
    private List<String> reviewerNames = new ArrayList<>();
    private boolean isReviewAccess;
    private Integer maxCountReview;
    private List<ProjectInfoReviewerDto> projects;
}
