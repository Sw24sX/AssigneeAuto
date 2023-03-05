package com.example.assigneeauto.assignee.include;

import com.example.assigneeauto.assignee.choose.assignee.MinimizationCountReview;
import com.example.assigneeauto.presets.domain.ReviewerPreset;
import com.example.assigneeauto.presets.test.MergeRequestPreset;
import com.example.assigneeauto.service.GitlabServiceApi;
import com.example.assigneeauto.service.ReviewerServiceApi;
import lombok.SneakyThrows;
import org.gitlab4j.api.Constants;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(locations="classpath:test.properties")
public class MinimizationCountReviewTests {
    @Autowired
    private MinimizationCountReview minimizationCountReview;

    @MockBean
    private GitlabServiceApi gitlabServiceApi;

    @MockBean
    private ReviewerServiceApi reviewerServiceApi;

    @SneakyThrows
    @Test
    public void MinCountOneUserTest() {
        var reviewer = ReviewerPreset.first();
        var mergeRequest = MergeRequestPreset.first();

        Mockito.when(reviewerServiceApi.getAllActive(mergeRequest.getProjectId().toString())).then(x -> List.of(reviewer));
        Mockito.when(gitlabServiceApi.getListMergeRequestByAssigneeId(reviewer.getMemberId(), Constants.MergeRequestState.OPENED, mergeRequest.getProjectId().toString()))
                .then(x -> List.of(MergeRequestPreset.second()));
        var result = minimizationCountReview.getWeight(reviewer, mergeRequest);
        assertThat(result).isEqualTo(100);
    }

    @SneakyThrows
    @Test
    public void MinCountTwoUserTest() {
        var reviewer = ReviewerPreset.first();
        var mergeRequest = MergeRequestPreset.first();

        Mockito.when(reviewerServiceApi.getAllActive(mergeRequest.getProjectId().toString())).then(x -> List.of(reviewer, ReviewerPreset.second()));
        Mockito.when(gitlabServiceApi.getListMergeRequestByAssigneeId(reviewer.getMemberId(), Constants.MergeRequestState.OPENED, mergeRequest.getProjectId().toString()))
                .then(x -> List.of(MergeRequestPreset.second()));
        Mockito.when(gitlabServiceApi.getListMergeRequestByAssigneeId(ReviewerPreset.second().getMemberId(), Constants.MergeRequestState.OPENED, mergeRequest.getProjectId().toString()))
                .then(x -> List.of(MergeRequestPreset.second(), MergeRequestPreset.first()));
        var result = minimizationCountReview.getWeight(reviewer, mergeRequest);
        assertThat(result).isEqualTo(100);
    }

    @SneakyThrows
    @Test
    public void MinCountThreeUserTest() {
        var reviewer = ReviewerPreset.first();
        var mergeRequest = MergeRequestPreset.first();

        Mockito.when(reviewerServiceApi.getAllActive(mergeRequest.getProjectId().toString())).then(x -> List.of(reviewer, ReviewerPreset.second(), ReviewerPreset.third()));
        Mockito.when(gitlabServiceApi.getListMergeRequestByAssigneeId(reviewer.getMemberId(), Constants.MergeRequestState.OPENED, mergeRequest.getProjectId().toString()))
                .then(x -> List.of(MergeRequestPreset.second()));
        Mockito.when(gitlabServiceApi.getListMergeRequestByAssigneeId(ReviewerPreset.second().getMemberId(), Constants.MergeRequestState.OPENED, mergeRequest.getProjectId().toString()))
                .then(x -> List.of(MergeRequestPreset.second(), MergeRequestPreset.first()));
        Mockito.when(gitlabServiceApi.getListMergeRequestByAssigneeId(ReviewerPreset.third().getMemberId(), Constants.MergeRequestState.OPENED, mergeRequest.getProjectId().toString()))
                .then(x -> List.of());
        var result = minimizationCountReview.getWeight(reviewer, mergeRequest);
        assertThat(result).isEqualTo(50);
    }
}
