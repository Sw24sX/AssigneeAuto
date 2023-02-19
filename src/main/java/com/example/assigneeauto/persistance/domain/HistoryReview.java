package com.example.assigneeauto.persistance.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Table
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class HistoryReview extends BaseEntity {

    private String branchName;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "reviewer_id")
    @ToString.Exclude
    private Reviewer reviewer;

    private Long mergeRequestIid;

    private String mergeRequestName;

    private boolean isSuccess;
}
