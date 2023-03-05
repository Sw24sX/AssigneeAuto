package com.example.assigneeauto.assignee.exclude;

import com.example.assigneeauto.assignee.excluded.assignee.MaxMergeRequestPerReviewer;
import com.example.assigneeauto.presets.test.MergeRequestPreset;
import com.example.assigneeauto.presets.domain.ReviewerPreset;
import com.example.assigneeauto.service.GitlabServiceApi;
import org.gitlab4j.api.Constants;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.MergeRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(properties = { "auto-assignee.excluded.part.max-merge-request-per-reviewer.max-merge-requests=2" })
@TestPropertySource(locations="classpath:test.properties")
public class MaxMergeRequestPerReviewerTests {
    @Autowired
    private MaxMergeRequestPerReviewer maxMergeRequestPerReviewer;

    @MockBean
    private GitlabServiceApi gitlabApiService;

    @BeforeEach
    public void setUp() throws GitLabApiException {
        var first = ReviewerPreset.first();
        var second = ReviewerPreset.second();
        List<MergeRequest> firstMergeRequests = List.of(
                MergeRequestPreset.first(),
                MergeRequestPreset.first(),
                MergeRequestPreset.first(),
                MergeRequestPreset.first(),
                MergeRequestPreset.first()
        );

        List<MergeRequest> secondMergeRequests = List.of(
                MergeRequestPreset.first()
        );

        Mockito.when(gitlabApiService.
                getListMergeRequestByAssigneeId(first.getMemberId(), Constants.MergeRequestState.OPENED, MergeRequestPreset.first().getProjectId().toString()))
                .then(x -> firstMergeRequests);

        Mockito.when(gitlabApiService.
                        getListMergeRequestByAssigneeId(second.getMemberId(), Constants.MergeRequestState.OPENED, MergeRequestPreset.first().getProjectId().toString()))
                .then(x -> secondMergeRequests);
    }

    @Test
    public void excludeReviewerWithFiveMR() {
        var reviewer = ReviewerPreset.first();
        MergeRequest request = MergeRequestPreset.second();

        assertThat(maxMergeRequestPerReviewer.excludeAssignee(reviewer, request)).isTrue();
    }

    @Test
    public void includeReviewerWithOneMR() {
        var reviewer = ReviewerPreset.second();
        MergeRequest request = MergeRequestPreset.second();

        assertThat(maxMergeRequestPerReviewer.excludeAssignee(reviewer, request)).isFalse();
    }
}
