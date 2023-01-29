package com.example.assigneeauto.service.impl;

import com.example.assigneeauto.persistance.exception.AutoAssigneeException;
import com.example.assigneeauto.persistance.properties.GitRepoProperties;
import com.example.assigneeauto.persistance.properties.GitlabApiProperties;
import com.example.assigneeauto.service.GitService;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.blame.BlameResult;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.HistogramDiff;
import org.eclipse.jgit.errors.RepositoryNotFoundException;
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
public class GitServiceImpl implements GitService {

    private static final String FULL_BRANCH_NAME_FORMAT = "refs/remotes/origin/%s";

    private final GitRepoProperties properties;
    private final GitlabApiProperties gitlabApiProperties;
    private final CredentialsProvider credentialsProvider;

    private final Git git;

    public GitServiceImpl(GitRepoProperties properties, GitlabApiProperties gitlabApiProperties) {
        this.properties = properties;
        this.gitlabApiProperties = gitlabApiProperties;
        this.credentialsProvider = new UsernamePasswordCredentialsProvider(gitlabApiProperties.getUsername(),
                gitlabApiProperties.getToken());
        this.git = getGit(properties, gitlabApiProperties);
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

    @Override
    public void updateRepository() {
        try {
            git.fetch()
                .setCredentialsProvider(credentialsProvider)
                .call();
        } catch (GitAPIException e) {
            throw new AutoAssigneeException(e.getMessage());
        }
        updateBranch(properties.getBaseBranchName());
    }

    @Override
    public void updateBranch(String branchName) {
        try {
            git.pull()
                .setRemoteBranchName(branchName)
                .setCredentialsProvider(credentialsProvider)
                .call();
        } catch (GitAPIException e) {
            throw new AutoAssigneeException(e.getMessage());
        }
    }

    @Override
    @Cacheable(value = "diff-branches", key = "#newBranchName")
    public List<DiffEntry> getDiffBranches(String newBranchName) {
        try {

            String oldFillBranchName = String.format(FULL_BRANCH_NAME_FORMAT, properties.getBaseBranchName());
            String newFullBranchName = String.format(FULL_BRANCH_NAME_FORMAT, newBranchName);

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
    public List<Ref> getAllBranches() {
        try {

            return git.branchList()
                .setListMode(ListBranchCommand.ListMode.ALL)
                .call();
        } catch (GitAPIException e) {
            throw new AutoAssigneeException(e.getMessage());
        }
    }

    @Override
    @Cacheable(value = "blame-file", key = "#fileFromRepo")
    public BlameResult getBlameFile(String fileFromRepo) {
        try {
            return git.blame()
                .setFilePath(fileFromRepo)
                .setDiffAlgorithm(new HistogramDiff())
                .call();
        } catch (GitAPIException e) {
            throw new AutoAssigneeException(e.getMessage());
        }
    }
}
