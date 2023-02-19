package com.example.assigneeauto.service.impl;

import com.example.assigneeauto.persistance.domain.Reviewer;
import com.example.assigneeauto.persistance.exception.AutoAssigneeException;
import com.example.assigneeauto.repository.ReviewerRepository;
import com.example.assigneeauto.service.GitlabServiceApi;
import com.example.assigneeauto.service.ReviewerServiceApi;
import lombok.RequiredArgsConstructor;
import org.gitlab4j.api.GitLabApiException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public Reviewer addNewReviewer(String username) throws GitLabApiException {
        Reviewer reviewer = reviewerRepository.findByUsername(username)
                .orElse(createReviewer(username));
        if (!reviewer.isReviewAccess()) {
            reviewer.setReviewAccess(true);
        }
        return reviewer;
    }

    private Reviewer createReviewer(String username) throws GitLabApiException {
        var member = gitlabServiceApi.getListMembers().stream()
                .filter(x -> x.getUsername().equals(username))
                .findFirst()
                .orElseThrow(() -> new AutoAssigneeException("Участник '%s' не найден в проекте", username));

        var reviewer = new Reviewer();
        reviewer.setReviewAccess(true);
        reviewer.setUsername(member.getUsername());
        reviewer.setMemberId(member.getId());
        reviewer.setAccessLevelGitLab(member.getAccessLevel());
        return reviewerRepository.save(reviewer);
    }

    @Override
    @Transactional
    public Reviewer saveReviewer(Reviewer reviewer) {
        return reviewerRepository.save(reviewer);
    }

    @Override
    public void deleteReviewer(Long id) {
        reviewerRepository.deleteById(id);
    }
}
