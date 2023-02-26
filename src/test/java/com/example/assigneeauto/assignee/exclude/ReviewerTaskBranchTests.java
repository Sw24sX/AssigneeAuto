package com.example.assigneeauto.assignee.exclude;

import com.example.assigneeauto.assignee.excluded.assignee.ReviewerTaskBranch;
import com.example.assigneeauto.presets.test.MergeRequestPreset;
import com.example.assigneeauto.presets.domain.ReviewerPreset;
import com.example.assigneeauto.repository.HistoryReviewRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@TestPropertySource(locations="classpath:test.properties")
public class ReviewerTaskBranchTests {

    @Autowired
    private ReviewerTaskBranch reviewerTaskBranch;

    @MockBean
    private HistoryReviewRepository historyReviewRepository;

    @Test
    public void ExcludeReviewerNotBranch_Ok() {
        var first = ReviewerPreset.first();
        var request = MergeRequestPreset.first();

        Mockito.when(historyReviewRepository.existsByBranchName("main"))
                .then(x -> true);

        Mockito.when(historyReviewRepository.existsByBranchNameAndReviewer_Id("main", first.getId()))
                .then(x -> true);

        var result = reviewerTaskBranch.excludeAssignee(first, request);
        assertThat(result).isTrue();
    }
}
