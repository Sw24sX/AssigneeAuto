package com.example.assigneeauto.persistance.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "history_review")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoryReview extends BaseEntity {

    @Column(name = "branch_name")
    private String branchName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewer_id")
    private Reviewer reviewer;
}
