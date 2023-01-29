package com.example.assigneeauto.controller;

import com.example.assigneeauto.persistance.domain.Reviewer;
import com.example.assigneeauto.service.ReviewerService;
import org.gitlab4j.api.GitLabApiException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("reviewer")
public class ReviewerController {
    private final ReviewerService reviewerService;

    public ReviewerController(ReviewerService reviewerService) {
        this.reviewerService = reviewerService;
    }

    @GetMapping
    public List<Reviewer> getAll() {
        return reviewerService.getAll();
    }

    @GetMapping("{id}")
    public Reviewer getById(@PathVariable("id") Long id) {

        return reviewerService.getById(id);
    }

    @PostMapping
    public Reviewer addReviewer(@RequestParam String username,
                                @RequestParam String gitUsername) throws GitLabApiException {

        return reviewerService.addNewReviewer(username, gitUsername);
    }

    @PutMapping("{id}")
    public Reviewer deleteAccessReviewer(@PathVariable("id") Long id) {
        Reviewer reviewer = reviewerService.getById(id);
        return reviewerService.deleteAccessReviewer(reviewer.getUsername());
    }
}
