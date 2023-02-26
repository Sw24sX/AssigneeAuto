package com.example.assigneeauto.presets.test;

import com.example.assigneeauto.presets.test.dto.ReviewerData;
import org.gitlab4j.api.models.Author;
import org.gitlab4j.api.models.MergeRequest;

public class MergeRequestPreset {
    public static MergeRequest first() {
        var reviewerData = ReviewerDataPreset.first();

        var request = new MergeRequest();
        request.setAuthor(createAuthor(reviewerData));
        request.setId(1L);
        request.setIid(1L);
        request.setState("OPEN");
        request.setProjectId(1L);
        request.setTitle("Test merge request first");
        request.setSourceBranch(GitBranch.TEST_1.getName());
        request.setTargetBranch(GitBranch.MAIN.getName());
        return request;
    }

    public static MergeRequest second() {
        var reviewerData = ReviewerDataPreset.second();

        MergeRequest request = new MergeRequest();
        request.setAuthor(createAuthor(reviewerData));
        request.setId(2L);
        request.setIid(2L);
        request.setState("OPEN");
        request.setProjectId(1L);
        request.setTitle("Test merge request second");
        request.setSourceBranch(GitBranch.TEST_2.getName());
        request.setTargetBranch(GitBranch.MAIN.getName());
        return request;
    }

    private static Author createAuthor(ReviewerData reviewerData) {
        var author = new Author();
        author.setUsername(reviewerData.getUsername());
        author.setEmail(reviewerData.getEmail());
        author.setName(reviewerData.getUsername());
        author.setId(Long.parseLong(reviewerData.getId()));
        return author;
    }
}
