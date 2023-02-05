package com.example.assigneeauto.controller;

import com.example.assigneeauto.persistance.domain.Reviewer;
import com.example.assigneeauto.service.ReviewerServiceApi;
import org.gitlab4j.api.GitLabApiException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("reviewer")
public class ReviewerController {
    private final ReviewerServiceApi reviewerServiceApi;

    public ReviewerController(ReviewerServiceApi reviewerServiceApi) {
        this.reviewerServiceApi = reviewerServiceApi;
    }

    @GetMapping
    public List<Reviewer> getAll() {
        return reviewerServiceApi.getAll();
    }

    @GetMapping("{id}")
    public Reviewer getById(@PathVariable("id") Long id) {

        return reviewerServiceApi.getById(id);
    }

    @PostMapping
    public Reviewer addReviewer(@RequestParam String username,
                                @RequestParam String gitUsername) throws GitLabApiException {

        return reviewerServiceApi.addNewReviewer(username, gitUsername);
    }

    @PutMapping("{id}")
    public Reviewer deleteAccessReviewer(@PathVariable("id") Long id) {
        Reviewer reviewer = reviewerServiceApi.getById(id);
        return reviewerServiceApi.deleteAccessReviewer(reviewer.getUsername());
    }
}
