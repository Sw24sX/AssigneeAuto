package com.example.assigneeauto.service.impl;

import com.example.assigneeauto.persistance.domain.Reviewer;
import com.example.assigneeauto.persistance.exception.AutoAssigneeException;
import com.example.assigneeauto.repository.ReviewerRepository;
import com.example.assigneeauto.service.GitlabServiceApi;
import com.example.assigneeauto.service.ReviewerServiceApi;
import lombok.RequiredArgsConstructor;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Member;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewerService implements ReviewerServiceApi {

    private final GitlabServiceApi gitlabServiceApi;
    private final ReviewerRepository reviewerRepository;

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
        Member member = gitlabServiceApi.getListMembers().stream()
                .filter(x -> x.getUsername().equals(username))
                .findFirst()
                .orElseThrow(() -> new AutoAssigneeException("Участник '%s' не найден в проекте", username));

        Reviewer reviewer = new Reviewer();
        reviewer.setReviewAccess(true);
        reviewer.setUsername(member.getUsername());
        reviewer.setMemberId(member.getId());
        reviewer.setAccessLevelGitLab(member.getAccessLevel());
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
        // TODO: 16.10.2022 Добавить поддержку нескольких имен в git для каждого ревьвера
        return reviewer.getGitUsername().equals(name);
    }
}
