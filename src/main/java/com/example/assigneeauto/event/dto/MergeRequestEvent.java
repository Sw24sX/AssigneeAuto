package com.example.assigneeauto.event.dto;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class MergeRequestEvent extends ApplicationEvent {

    private final Long mergeRequestIid;
    private final Long projectId;

    public MergeRequestEvent(Object source, Long mergeRequestIid, Long projectId) {
        super(source);
        this.mergeRequestIid = mergeRequestIid;
        this.projectId = projectId;
    }
}
