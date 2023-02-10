package com.example.assigneeauto.persistance.domain;

import lombok.*;
import org.gitlab4j.api.models.AccessLevel;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "reviewer")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Reviewer extends BaseEntity {

    @Column(name = "access_level_gitlab")
    private AccessLevel accessLevelGitLab;

    private String username;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "review_access")
    private boolean isReviewAccess;

    @Column(name = "git_username")
    private String gitUsername;

    @OneToMany(mappedBy = "reviewer", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<HistoryReview> historyReviews;

    @OneToMany(mappedBy = "reviewer", fetch = FetchType.EAGER)
    @ToString.Include
    private Set<ReviewerName> reviewerNames;
}
