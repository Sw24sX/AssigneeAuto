package com.example.assigneeauto.assignee.exclude;

import com.example.assigneeauto.assignee.excluded.assignee.ReviewerTaskBranch;
import com.example.assigneeauto.persistance.domain.Reviewer;
import com.example.assigneeauto.presets.domain.HistoryReviewPreset;
import com.example.assigneeauto.presets.test.GitBranch;
import com.example.assigneeauto.presets.test.MergeRequestPreset;
import com.example.assigneeauto.presets.domain.ReviewerPreset;
import com.example.assigneeauto.repository.HistoryReviewRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@TestPropertySource(locations="classpath:test.properties")
public class ReviewerTaskBranchTests {

    @Autowired
    private ReviewerTaskBranch reviewerTaskBranch;

    @MockBean
    private HistoryReviewRepository historyReviewRepository;

    @Test
    public void includeReviewerBranch() {
        var reviewer = ReviewerPreset.first();
        var mergeRequest = MergeRequestPreset.first();
        var history = HistoryReviewPreset.first();

        Mockito.when(historyReviewRepository.findByBranchName(GitBranch.TEST_1.getName()))
                .then(x -> history);

        var result = reviewerTaskBranch.excludeAssignee(reviewer, mergeRequest);
        assertThat(result).isFalse();
    }

    @ParameterizedTest
    @MethodSource("reviewersNotInHistory")
    public void excludeReviewersNotBranch(Reviewer reviewer) {
        var mergeRequest = MergeRequestPreset.first();
        var history = HistoryReviewPreset.first();

        Mockito.when(historyReviewRepository.findByBranchName(GitBranch.TEST_1.getName()))
                .then(x -> history);

        var result = reviewerTaskBranch.excludeAssignee(reviewer, mergeRequest);
        assertThat(result).isTrue();
    }

    @ParameterizedTest
    @MethodSource("reviewersNotInHistory")
    public void includeReviewersWithoutHistory(Reviewer reviewer) {
        var mergeRequest = MergeRequestPreset.first();

        Mockito.when(historyReviewRepository.findByBranchName(GitBranch.TEST_1.getName()))
                .then(x -> null);

        var result = reviewerTaskBranch.excludeAssignee(reviewer, mergeRequest);
        assertThat(result).isFalse();
    }

    @ParameterizedTest
    @MethodSource("reviewersNotInHistory")
    public void includeReviewersWithInactiveHistoryReviewer(Reviewer reviewer) {
        var mergeRequest = MergeRequestPreset.first();
        var history = HistoryReviewPreset.reviewerInactiveSuccess();

        Mockito.when(historyReviewRepository.findByBranchName(GitBranch.TEST_1.getName()))
                .then(x -> history);

        var result = reviewerTaskBranch.excludeAssignee(reviewer, mergeRequest);
        assertThat(result).isFalse();
    }

    private static Stream<Reviewer> reviewersNotInHistory() {
        return Stream.of(
                ReviewerPreset.second(),
                ReviewerPreset.inactive()
        );
    }
}
