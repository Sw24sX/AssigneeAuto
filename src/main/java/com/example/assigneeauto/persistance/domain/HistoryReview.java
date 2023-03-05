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

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "reviewer_id", insertable = false, updatable = false)
    @ToString.Exclude
    private Reviewer reviewer;

    @Column(name = "reviewer_id")
    private Long reviewerId;

    private Long mergeRequestIid;

    private String mergeRequestName;

    private boolean isSuccess;
}
