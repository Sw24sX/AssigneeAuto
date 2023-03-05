package com.example.assigneeauto.event;

import com.example.assigneeauto.event.dto.MergeRequestEvent;
import com.example.assigneeauto.service.MergeRequestServiceApi;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

@Component
@EnableAsync
@RequiredArgsConstructor
public class MergeRequestEventListener {

    private final MergeRequestServiceApi mergeRequestServiceApi;

    @Async
    @EventListener
    public void mergeRequestListener(MergeRequestEvent event) {
        mergeRequestServiceApi.setAutoAssigneeOrIgnore(event.getMergeRequestIid(), event.getProjectId().toString());
    }
}
