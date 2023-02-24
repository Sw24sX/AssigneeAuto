package com.example.assigneeauto.service.impl;

import com.example.assigneeauto.persistance.domain.Reviewer;
import com.example.assigneeauto.persistance.exception.AutoAssigneeException;
import com.example.assigneeauto.persistance.mapper.ReviewerMapper;
import com.example.assigneeauto.repository.ReviewerRepository;
import com.example.assigneeauto.service.GitlabServiceApi;
import com.example.assigneeauto.service.ReviewerServiceApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewerService implements ReviewerServiceApi {

    private final GitlabServiceApi gitlabServiceApi;
    private final ReviewerRepository reviewerRepository;
    private final ReviewerMapper reviewerMapper;

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
        if (id == null) {
            return null;
        }

        return reviewerRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteReviewer(Long id) {
        reviewerRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Reviewer updateReviewer(Reviewer updated) {
        var reviewer = Optional.ofNullable(getById(updated.getId()))
                .orElseGet(() ->  createReviewer(updated.getUsername()));
        reviewerMapper.updateReviewer(updated, reviewer);
        return reviewerRepository.save(reviewer);
    }

    @Override
    public Reviewer initReviewer() {
        var result = new Reviewer();
        result.setReviewAccess(true);
        result.setReviewerNames(List.of(""));
        return result;
    }

    private Reviewer createReviewer(String username) {
        Member member;
        try {
            member = gitlabServiceApi.getListMembers().stream()
                    .filter(x -> x.getUsername().equals(username))
                    .findFirst()
                    .orElseThrow(() -> new AutoAssigneeException("Участник '%s' не найден в проекте", username));
        } catch (GitLabApiException e) {
            log.warn(e.getMessage(), e.getCause());
            throw new AutoAssigneeException(e.getMessage());
        }

        //TODO: to mapper
        var reviewer = new Reviewer();
        reviewer.setReviewAccess(true);
        reviewer.setUsername(member.getUsername());
        reviewer.setMemberId(member.getId());
        reviewer.setAccessLevelGitLab(member.getAccessLevel());
        return reviewerRepository.save(reviewer);
    }
}
