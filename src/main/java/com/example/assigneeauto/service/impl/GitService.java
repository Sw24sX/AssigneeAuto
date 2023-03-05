package com.example.assigneeauto.service.impl;

import com.example.assigneeauto.persistance.domain.ProjectInfo;
import com.example.assigneeauto.persistance.exception.AutoAssigneeException;
import com.example.assigneeauto.persistance.properties.GitRepoProperties;
import com.example.assigneeauto.persistance.properties.GitlabApiProperties;
import com.example.assigneeauto.service.GitServiceApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.blame.BlameResult;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.HistogramDiff;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@RequiredArgsConstructor
@Slf4j
public class GitService implements GitServiceApi {

    private static final String FULL_BRANCH_NAME_FORMAT = "refs/remotes/origin/%s";

    private final GitRepoProperties properties;
    private final CredentialsProvider credentialsProvider;
    private final GitlabApiProperties gitlabApiProperties;

    @Override
    public void updateRepository(String sourceBranch, String targetBranch, ProjectInfo projectInfo) {

        var path = getPath(projectInfo);
        try(var git = Git.open(path)) {
            git.fetch()
                .setCredentialsProvider(credentialsProvider)
                .call();
        } catch (GitAPIException | IOException e) {
            throw new AutoAssigneeException(e.getMessage());
        }
        updateBranch(sourceBranch, targetBranch, projectInfo);
    }

    @Override
    public void updateBranch(String sourceBranch, String targetBranch, ProjectInfo projectInfo) {

        var path = getPath(projectInfo);
        try(var git = Git.open(path)) {
            git.pull()
                .setRemoteBranchName(sourceBranch)
                .setCredentialsProvider(credentialsProvider)
                .call();
            git.pull()
                    .setRemoteBranchName(targetBranch)
                    .setCredentialsProvider(credentialsProvider)
                    .call();
        } catch (GitAPIException | IOException e) {
            throw new AutoAssigneeException(e.getMessage());
        }
    }

    @Override
    @Cacheable(value = "diff-branches", key = "#sourceBranch")
    public List<DiffEntry> getDiffBranches(String sourceBranch, String targetBranch, ProjectInfo projectInfo) {

        var path = getPath(projectInfo);
        try(var git = Git.open(path)) {
            String oldFillBranchName = String.format(FULL_BRANCH_NAME_FORMAT, targetBranch);
            String newFullBranchName = String.format(FULL_BRANCH_NAME_FORMAT, sourceBranch);

            Repository repository = git.getRepository();
            AbstractTreeIterator oldTreeParser = prepareTreeParser(repository, oldFillBranchName);
            AbstractTreeIterator newTreeParser = prepareTreeParser(repository, newFullBranchName);

            return git.diff().setOldTree(oldTreeParser).setNewTree(newTreeParser).call();
        } catch (IOException | GitAPIException e) {
            throw new AutoAssigneeException(e.getMessage());
        }
    }

    private AbstractTreeIterator prepareTreeParser(Repository repository, String ref) throws IOException {
        Ref head = repository.exactRef(ref);
        try (RevWalk walk = new RevWalk(repository)) {
            RevCommit commit = walk.parseCommit(head.getObjectId());
            RevTree tree = walk.parseTree(commit.getTree().getId());

            CanonicalTreeParser treeParser = new CanonicalTreeParser();
            try (ObjectReader reader = repository.newObjectReader()) {
                treeParser.reset(reader, tree.getId());
            }
            walk.dispose();

            return treeParser;
        }
    }

    @Override
    @Cacheable(value = "blame-file", key = "#fileFromRepo")
    public BlameResult getBlameFile(String fileFromRepo, ProjectInfo projectInfo) {

        var path = getPath(projectInfo);
        try(var git = Git.open(path)) {
            return git.blame()
                .setFilePath(fileFromRepo)
                .setDiffAlgorithm(new HistogramDiff())
                .call();
        } catch (GitAPIException | IOException e) {
            throw new AutoAssigneeException(e.getMessage());
        }
    }

    @Override
    public boolean cloneRepository(String url, ProjectInfo projectInfo) {

        var path = getPath(projectInfo);
        try {
            CredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider(
                    gitlabApiProperties.getUsername(),
                    gitlabApiProperties.getToken());

            Git.cloneRepository()
                .setDirectory(path)
                .setURI(url)
                .setCredentialsProvider(credentialsProvider)
                .call();
            return true;
        } catch (GitAPIException e) {
            log.warn(e.getMessage(), e.getCause());
            return false;
        }
    }

    private File getPath(ProjectInfo projectInfo) {
        var path = new File(properties.getPath() + projectInfo.getName());
        return path;
    }
}
