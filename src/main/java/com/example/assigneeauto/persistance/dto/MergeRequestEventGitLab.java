package com.example.assigneeauto.persistance.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import org.gitlab4j.api.models.User;
import org.gitlab4j.api.webhook.EventProject;

import java.time.LocalDate;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class MergeRequestEventGitLab {
    private User user;
    private EventProject project;
    private ObjectAttribute objectAttributes;

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class ObjectAttribute {

        private Long authorId;
        private Long id;
        private Long iid;
        private String title;
        private String targetBranch;
        private String sourceBranch;
    }
}
