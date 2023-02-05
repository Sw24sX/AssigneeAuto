package com.example.assigneeauto.config;

import com.example.assigneeauto.persistance.exception.AutoAssigneeException;
import com.example.assigneeauto.persistance.properties.GitRepoProperties;
import com.example.assigneeauto.persistance.properties.GitlabApiProperties;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.RepositoryNotFoundException;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;

@Configuration
public class GitConfig {

    @Bean
    public CredentialsProvider credentialsProvider(GitlabApiProperties gitlabApiProperties) {
        return new UsernamePasswordCredentialsProvider(gitlabApiProperties.getUsername(), gitlabApiProperties.getToken());
    }

    @Bean
    public Git git(GitRepoProperties properties, GitlabApiProperties gitlabApiProperties) {
        return getGit(properties, gitlabApiProperties);
    }

    private static Git getGit(GitRepoProperties properties, GitlabApiProperties gitlabApiProperties) {
        try {

            return Git.open(new File(properties.getPath()));
        } catch (RepositoryNotFoundException e) {

            return cloneRepository(properties, gitlabApiProperties);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Git cloneRepository(GitRepoProperties properties, GitlabApiProperties gitlabApiProperties) {
        try {
            CredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider(
                    gitlabApiProperties.getUsername(),
                    gitlabApiProperties.getToken());

            return Git.cloneRepository()
                    .setDirectory(new File(properties.getPath()))
                    .setURI(properties.getUrl())
                    .setCredentialsProvider(credentialsProvider)
                    .call();
        } catch (GitAPIException e) {
            throw new AutoAssigneeException(e.getMessage());
        }
    }
}
