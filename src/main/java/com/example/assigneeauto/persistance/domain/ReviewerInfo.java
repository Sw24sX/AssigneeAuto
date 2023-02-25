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
public class ReviewerInfo extends BaseEntity {

    private Integer maxCountReview;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "reviewer_id", referencedColumnName = "id")
    private Reviewer reviewer;
}
