package com.example.assigneeauto.persistance.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "git.lab.api")
@Component
@Data
public class GitlabApiProperties {

    /**
     * Базовый url gitlab
     */
    private String url;

    /**
     * Токен доступа к gitlab
     */
    private String token;

    /**
     * id проекта
     */
    private String projectId;

    /**
     * Имя пользователя для доступа к gitlab
     */
    private String username;
}
