package com.example.assigneeauto.presets.test.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ReviewerData {

    private String id;
    private List<String> name;
    private String username;
    private String state;
    private boolean isActive;
    private String email;
    private Integer maxCountReview;
}
