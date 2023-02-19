package com.example.assigneeauto.persistance.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "reviewer_name")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"gitName"}, callSuper = false)
public class ReviewerName extends BaseEntity {

    public ReviewerName(String gitName) {
        this.gitName = gitName;
    }

    private String gitName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "reviewer_id")
    @ToString.Exclude
    private Reviewer reviewer;
}
