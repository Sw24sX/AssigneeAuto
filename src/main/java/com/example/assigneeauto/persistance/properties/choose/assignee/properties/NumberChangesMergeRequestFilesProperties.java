package com.example.assigneeauto.persistance.properties.choose.assignee.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "auto-assignee.choose.part.number-changes-merge-request-files")
@Data
@Component
public class NumberChangesMergeRequestFilesProperties extends BaseChooseAssigneeProperties {
}
