package com.example.assigneeauto.presets.test;

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
                .build();
    }

    public static ReviewerData second() {
        return ReviewerData
                .builder()
                .id("2")
                .isActive(true)
                .name(List.of("No_Test", "no_test"))
                .username("test_2")
                .email("test_2@mail.ru")
                .maxCountReview(4)
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
                .build();
    }
}
