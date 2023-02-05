package com.example.assigneeauto.config;

import com.example.assigneeauto.persistance.properties.GitlabApiProperties;
import org.gitlab4j.api.GitLabApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GitlabConfig {
    @Bean
    public GitLabApi gitLabApi(GitlabApiProperties gitlabApiProperties) {
        return new GitLabApi(gitlabApiProperties.getUrl(), gitlabApiProperties.getToken());
    }
}
