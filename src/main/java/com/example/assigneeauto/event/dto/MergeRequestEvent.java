package com.example.assigneeauto.event.dto;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class MergeRequestEvent extends ApplicationEvent {

    private final Long mergeRequestIid;

    public MergeRequestEvent(Object source, Long mergeRequestIid) {
        super(source);
        this.mergeRequestIid = mergeRequestIid;
    }
}
