package com.example.assigneeauto.persistance.domain;

import lombok.*;
import org.gitlab4j.api.models.AccessLevel;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Reviewer extends BaseEntity {

    @Column(name = "access_level_gitlab")
    private AccessLevel accessLevelGitLab;

    private String username;

    private Long memberId;

    @Column(name = "review_access")
    private boolean isReviewAccess;

    @OneToMany(mappedBy = "reviewer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<HistoryReview> historyReviews;

    @ElementCollection
    @CollectionTable(name = "reviewer_name", joinColumns = @JoinColumn(name = "reviewer_id"))
    @Column(name = "git_name")
    private List<String> reviewerNames;

    @OneToOne(mappedBy = "reviewer", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private ReviewerInfo info;
}
