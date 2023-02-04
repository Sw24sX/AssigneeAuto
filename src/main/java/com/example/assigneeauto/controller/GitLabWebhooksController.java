package com.example.assigneeauto.controller;

import com.example.assigneeauto.persistance.dto.MergeRequestEventGitLab;
import com.example.assigneeauto.service.MergeRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("gitlab/event")
public class GitLabWebhooksController {

    private final MergeRequestService mergeRequestService;

    @PostMapping("merge-request")
    public void mergeRequestEvent(@RequestBody MergeRequestEventGitLab event) {
        var mergeRequestIid = event.getObjectAttributes().getIid();
        mergeRequestService.setAutoAssigneeOrIgnore(mergeRequestIid);
    }
}
