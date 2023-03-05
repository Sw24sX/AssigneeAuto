package com.example.assigneeauto.persistance.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "git.repo")
@Component
@Data
public class GitRepoProperties {

    /**
     * Путь для локального расположения репозитория
     */
    private String path;
}
