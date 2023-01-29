package com.example.assigneeauto.persistance.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gitlab4j.api.models.AccessLevel;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "reviewer")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reviewer extends BaseEntity {

    @Column(name = "access_level_gitlab1")
    private AccessLevel accessLevel;

    @Column(name = "username")
    private String username;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "review_access")
    private boolean isReviewAccess;

    @Column(name = "git_username")
    private String gitUsername;

    @OneToMany(mappedBy = "reviewer", fetch = FetchType.LAZY)
    private List<HistoryReview> historyReviews;
}
