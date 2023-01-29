package com.example.assigneeauto.controller;

import com.example.assigneeauto.persistance.domain.Reviewer;
import com.example.assigneeauto.service.MergeRequestService;
import com.example.assigneeauto.service.ReviewerService;
import org.gitlab4j.api.models.MergeRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("merge-request")
public class MergeRequestController {

    private final MergeRequestService mergeRequestService;
    private final ReviewerService reviewerService;

    public MergeRequestController(MergeRequestService mergeRequestService, ReviewerService reviewerService) {
        this.mergeRequestService = mergeRequestService;
        this.reviewerService = reviewerService;
    }

    @PostMapping("{merge-request-iid}/reviewer/{reviewer-id}")
    public void setAssigneeMergeRequest(@PathVariable("reviewer-id") Long reviewerId,
                                        @PathVariable("merge-request-iid") Long mergeRequestIid) {

        Reviewer reviewer = reviewerService.getById(reviewerId);
        mergeRequestService.setAssignee(mergeRequestIid, reviewer.getMemberId());
    }

    @PostMapping("{merge-request-iid}")
    public MergeRequest setAutoAssignee(@PathVariable("merge-request-iid") Long mergeRequestIid) {

        return mergeRequestService.setAutoAssignee(mergeRequestIid);
    }
}
