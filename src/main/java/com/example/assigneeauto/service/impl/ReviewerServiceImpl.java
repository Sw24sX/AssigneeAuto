package com.example.assigneeauto.service.impl;

import com.example.assigneeauto.persistance.domain.Reviewer;
import com.example.assigneeauto.persistance.exception.AutoAssigneeException;
import com.example.assigneeauto.repository.ReviewerRepository;
import com.example.assigneeauto.service.GitlabApiService;
import com.example.assigneeauto.service.ReviewerService;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Member;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewerServiceImpl implements ReviewerService {

    private final GitlabApiService gitlabApiService;
    private final ReviewerRepository reviewerRepository;

    public ReviewerServiceImpl(GitlabApiService gitlabApiService, ReviewerRepository reviewerRepository) {
        this.gitlabApiService = gitlabApiService;
        this.reviewerRepository = reviewerRepository;
    }

    @Override
    public List<Reviewer> getAll() {
        return reviewerRepository.findAll();
    }

    @Override
    public List<Reviewer> getAllActive() {
        return reviewerRepository.findAllByReviewAccess(true);
    }

    @Override
    public Reviewer getById(Long id) {
        return reviewerRepository.findById(id)
                .orElseThrow(() -> new AutoAssigneeException("Участник с id '%s' не найден в базе данных", id.toString()));
    }

    @Override
    public Reviewer addNewReviewer(String username, String gitUsername) throws GitLabApiException {
        Reviewer reviewer = reviewerRepository.findByUsernameOrGitUsername(username, gitUsername)
                .orElse(createReviewer(username, gitUsername));
        if (!reviewer.isReviewAccess()) {
            reviewer.setReviewAccess(true);
        }
        return reviewer;
    }

    private Reviewer createReviewer(String username, String gitUsername) throws GitLabApiException {
        Member member = gitlabApiService.getListMembers().stream()
                .filter(x -> x.getUsername().equals(username))
                .findFirst()
                .orElseThrow(() -> new AutoAssigneeException("Участник '%s' не найден в проекте", username));

        Reviewer reviewer = new Reviewer();
        reviewer.setReviewAccess(true);
        reviewer.setUsername(member.getUsername());
        reviewer.setMemberId(member.getId());
        reviewer.setAccessLevel(member.getAccessLevel());
        reviewer.setGitUsername(gitUsername);
        return reviewerRepository.save(reviewer);
    }

    @Override
    public Reviewer deleteAccessReviewer(String username) {
        Reviewer reviewer = reviewerRepository.findByUsername(username)
                .orElseThrow(() -> new AutoAssigneeException("Участник '%s' не найден в проекте", username));
        reviewer.setReviewAccess(false);
        return updateReviewer(reviewer);
    }

    @Override
    public Reviewer updateReviewer(Reviewer reviewer) {
        return reviewerRepository.save(reviewer);
    }

    @Override
    public boolean isReviewerGitName(Reviewer reviewer, String name) {
        // TODO: 16.10.2022 Добавить более серьезную проверку на соответствие ревьювера имени в репозитории
        return reviewer.getGitUsername().equals(name);
    }
}
