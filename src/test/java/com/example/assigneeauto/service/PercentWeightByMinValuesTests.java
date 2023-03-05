package com.example.assigneeauto.service;

import com.example.assigneeauto.persistance.dto.PercentWeightByMinMaxSettings;
import com.example.assigneeauto.presets.domain.ReviewerPreset;
import com.example.assigneeauto.presets.test.MergeRequestPreset;
import com.example.assigneeauto.service.dto.ReviewerWeight;
import com.example.assigneeauto.service.impl.PercentWeightByMinMaxValues;
import com.example.assigneeauto.service.mock.MockWeightByNotValues;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class PercentWeightByMinValuesTests {

    @Autowired
    private PercentWeightByMinMaxValues percentWeightByMinMaxValues;

    @MockBean
    private ReviewerServiceApi reviewerServiceApi;

    @ParameterizedTest
    @MethodSource("correctWeightNoRevertTest")
    public void correctWeightNoRevertTest(List<ReviewerWeight> reviewers) {
        correctWeightTest(reviewers, false);
    }

    private void correctWeightTest(List<ReviewerWeight> reviewers, boolean isRevert) {
        var mock = new MockWeightByNotValues(
                reviewers.stream()
                        .collect(Collectors.toMap(ReviewerWeight::getReviewer, ReviewerWeight::getInputWeight))
        );
        var allReviewers = reviewers.stream()
                .map(ReviewerWeight::getReviewer)
                .toList();
        var mergeRequest = MergeRequestPreset.first();
        Mockito.when(reviewerServiceApi.getAllActive(mergeRequest.getProjectId().toString()))
                .then(x -> allReviewers);
        for(var reviewer : reviewers) {
            var settings = PercentWeightByMinMaxSettings
                    .builder()
                    .reviewer(reviewer.getReviewer())
                    .mergeRequest(mergeRequest)
                    .weightByNotValuesApi(mock)
                    .isRevert(isRevert)
                    .build();
            assertThat(percentWeightByMinMaxValues.getCorrectWeight(settings)).isEqualTo(reviewer.getAnswer());
        }
    }

    public static Stream<List<ReviewerWeight>> correctWeightNoRevertTest() {
        var first = ReviewerPreset.first();
        var second = ReviewerPreset.second();
        var third = ReviewerPreset.third();
        var fourth = ReviewerPreset.fourth();
        return Stream.of(
                List.of(
                    new ReviewerWeight(first, 0L, 0),
                    new ReviewerWeight(second, 1L, 100)
                ),
                List.of(
                        new ReviewerWeight(first, 1L, 100),
                        new ReviewerWeight(second, 0L, 0)
                ),
                List.of(
                        new ReviewerWeight(first, 0L, 0),
                        new ReviewerWeight(second, 1L, 50),
                        new ReviewerWeight(third, 2L, 100)
                ),
                List.of(
                        new ReviewerWeight(first, -1L, 0),
                        new ReviewerWeight(second, 0L, 50),
                        new ReviewerWeight(third, 1L, 100)
                ),
                List.of(
                        new ReviewerWeight(third, 2L, 100),
                        new ReviewerWeight(second, 1L, 50),
                        new ReviewerWeight(first, 0L, 0)
                ),
                List.of(
                        new ReviewerWeight(third, 0L, 0),
                        new ReviewerWeight(second, 1L, 50),
                        new ReviewerWeight(fourth, 1L, 50),
                        new ReviewerWeight(first, 2L, 100)
                ),
                List.of(
                        new ReviewerWeight(third, 0L, 0)
                )
        );
    }
}
