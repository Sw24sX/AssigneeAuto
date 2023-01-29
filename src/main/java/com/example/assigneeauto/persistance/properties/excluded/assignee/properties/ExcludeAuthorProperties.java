package com.example.assigneeauto.persistance.properties.excluded.assignee.properties;

import com.example.assigneeauto.persistance.properties.BaseAutoAssigneePartProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "auto-assignee.excluded.part.exclude-author")
@Data
@Component
public class ExcludeAuthorProperties extends BaseAutoAssigneePartProperties {
}
