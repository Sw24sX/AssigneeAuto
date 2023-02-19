package com.example.assigneeauto.persistance.dto.web;

import lombok.Data;

@Data
public class HistoryDto {

    private String reviewerUsername;
    private String mergeRequestName;
    private boolean isSuccess;
}
