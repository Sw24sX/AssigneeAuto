package com.example.assigneeauto.controller;

import com.example.assigneeauto.service.MergeRequestServiceApi;
import com.example.assigneeauto.service.ReviewerServiceApi;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("merge-request")
@RequiredArgsConstructor
public class MergeRequestController {

    private final MergeRequestServiceApi mergeRequestServiceApi;
    private final ReviewerServiceApi reviewerServiceApi;

    @PostMapping("{merge-request-iid}/reviewer/{reviewer-id}")
    public void setAssigneeMergeRequest(@PathVariable("reviewer-id") Long reviewerId,
                                        @RequestParam("project-id") String projectId,
                                        @PathVariable("merge-request-iid") Long mergeRequestIid) {

        var reviewer = reviewerServiceApi.getById(reviewerId);
        mergeRequestServiceApi.setAssignee(mergeRequestIid, reviewer, projectId);
    }

    @PostMapping("{merge-request-iid}")
    public void setAutoAssignee(@PathVariable("merge-request-iid") Long mergeRequestIid, @RequestParam("project-id") String projectId) {
        var mergeRequest = mergeRequestServiceApi.getMergeRequestGitLab(mergeRequestIid, projectId);
        mergeRequestServiceApi.setAutoAssignee(mergeRequest);
    }
}
