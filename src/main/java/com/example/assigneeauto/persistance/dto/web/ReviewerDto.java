package com.example.assigneeauto.persistance.dto.web;

import lombok.Data;

@Data
public class ReviewerDto {
    private Long id;
    private String username;
    private String reviewerNames;
    private boolean isReviewAccess;
}
