package com.example.assigneeauto.service.dto;

import com.example.assigneeauto.persistance.domain.Reviewer;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReviewerWeight {
    private Reviewer reviewer;
    private Long inputWeight;
    private Integer answer;
}
