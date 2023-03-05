package com.example.assigneeauto.config;

import com.example.assigneeauto.persistance.properties.GitlabApiProperties;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GitConfig {

    @Bean
    public CredentialsProvider credentialsProvider(GitlabApiProperties gitlabApiProperties) {
        return new UsernamePasswordCredentialsProvider(gitlabApiProperties.getUsername(), gitlabApiProperties.getToken());
    }
}
