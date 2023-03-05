package com.example.assigneeauto.presets.test;

import com.example.assigneeauto.presets.domain.ProjectInfoPreset;
import com.example.assigneeauto.presets.test.dto.ReviewerData;

import java.util.List;

public class ReviewerDataPreset {
    public static ReviewerData first() {
        return ReviewerData
                .builder()
                .id("1")
                .isActive(true)
                .name(List.of("Test", "test"))
                .username("test_1")
                .email("test_1@mail.ru")
                .maxCountReview(4)
                .projectInfo(ProjectInfoPreset.first())
                .build();
    }

    public static ReviewerData second() {
        return ReviewerData
                .builder()
                .id("2")
                .isActive(true)
                .name(List.of("No_Test_2", "no_test_2"))
                .username("test_2")
                .email("test_2@mail.ru")
                .maxCountReview(4)
                .projectInfo(ProjectInfoPreset.first())
                .build();
    }

    public static ReviewerData third() {
        return ReviewerData
                .builder()
                .id("3")
                .isActive(true)
                .name(List.of("No_Test_3", "no_test_3"))
                .username("test_3")
                .email("test_3@mail.ru")
                .maxCountReview(4)
                .projectInfo(ProjectInfoPreset.first())
                .build();
    }

    public static ReviewerData fourth() {
        return ReviewerData
                .builder()
                .id("4")
                .isActive(true)
                .name(List.of("No_Test_4", "no_test_4"))
                .username("test_4")
                .email("test_4@mail.ru")
                .maxCountReview(4)
                .projectInfo(ProjectInfoPreset.first())
                .build();
    }

    public static ReviewerData fifth() {
        return ReviewerData
                .builder()
                .id("5")
                .isActive(true)
                .name(List.of("No_Test_5", "no_test_5"))
                .username("test_5")
                .email("test_5@mail.ru")
                .maxCountReview(4)
                .projectInfo(ProjectInfoPreset.first())
                .build();
    }

    public static ReviewerData inactive() {
        return ReviewerData
                .builder()
                .id("3")
                .isActive(false)
                .name(List.of("inactive"))
                .username("inactive")
                .maxCountReview(4)
                .projectInfo(ProjectInfoPreset.first())
                .build();
    }
}
