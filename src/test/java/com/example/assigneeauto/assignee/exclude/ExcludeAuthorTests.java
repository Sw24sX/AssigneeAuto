package com.example.assigneeauto.assignee.exclude;

import com.example.assigneeauto.assignee.excluded.assignee.ExcludeAuthor;
import com.example.assigneeauto.presets.test.MergeRequestPreset;
import com.example.assigneeauto.presets.domain.ReviewerPreset;
import org.gitlab4j.api.models.MergeRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest
@TestPropertySource(locations="classpath:test.properties")
public class ExcludeAuthorTests {

    @Autowired
    private ExcludeAuthor excludeAuthor;

    @Test
    public void excludeAuthor() {
        var mergeRequest = MergeRequestPreset.first();
        var reviewer = ReviewerPreset.first();

        var result = excludeAuthor.excludeAssignee(reviewer, mergeRequest);
        assertThat(result).isTrue();
    }

    @Test
    public void includeNotAuthor() {
        MergeRequest mergeRequest = MergeRequestPreset.first();
        var reviewer = ReviewerPreset.second();

        var result = excludeAuthor.excludeAssignee(reviewer, mergeRequest);
        assertThat(result).isFalse();
    }
}
