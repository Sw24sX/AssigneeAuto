package com.example.assigneeauto.persistance.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "git.repo")
@Component
@Data
public class GitRepoProperties {
    /**
     * Ссылка на скачивание git репозитория
     */
    private String url;

    /**
     * Путь для локального расположения репозитория
     */
    private String path;

    /**
     * Название базовой ветки, в которую будут идти MR
     */
    private String baseBranchName;
}
