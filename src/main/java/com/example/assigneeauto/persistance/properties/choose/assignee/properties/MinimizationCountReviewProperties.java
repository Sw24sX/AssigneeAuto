package com.example.assigneeauto.persistance.properties.choose.assignee.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "auto-assignee.choose.part.minimize-count-review")
@Data
@Component
public class MinimizationCountReviewProperties extends BaseChooseAssigneeProperties {
}
