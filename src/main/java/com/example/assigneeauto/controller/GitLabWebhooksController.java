package com.example.assigneeauto.controller;

import com.example.assigneeauto.event.dto.MergeRequestEvent;
import com.example.assigneeauto.persistance.dto.MergeRequestEventGitLab;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("gitlab/event")
public class GitLabWebhooksController {

    private final ApplicationEventPublisher applicationEventPublisher;

    @PostMapping("merge-request")
    public void mergeRequestEvent(@RequestBody MergeRequestEventGitLab mergeRequestEventGitLab) {
        var mergeRequestIid = mergeRequestEventGitLab.getObjectAttributes().getIid();
        var projectId = mergeRequestEventGitLab.getProject().getId();
        var event = new MergeRequestEvent(this, mergeRequestIid, projectId);
        //Отправляем эвент на асинхронную обработку, т.к. обработка пришедшего запроса может затянуться
        //из-за чего GitLab может нас заблочить по неуспешно обрабатываемым запросам по времени
        applicationEventPublisher.publishEvent(event);
    }
}
