package com.example.assigneeauto.service.impl;

import com.example.assigneeauto.persistance.domain.Reviewer;
import com.example.assigneeauto.persistance.mapper.ReviewerMapper;
import com.example.assigneeauto.repository.ReviewerRepository;
import com.example.assigneeauto.service.GitlabServiceApi;
import com.example.assigneeauto.service.ReviewerServiceApi;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public Map<String, String> updateReviewer(Reviewer updated) {
        var reviewer = getById(updated.getId());
        if (reviewer == null) {
            var errors = new HashMap<String, String>();
            errors.put("id", "Не удалось найти ревьювера с таким Id");
            return errors;
        }
        reviewerMapper.updateReviewer(updated, reviewer);
        var errors = validateReviewer(reviewer);
        if (!errors.isEmpty()) {
            return errors;
        }
        reviewerRepository.save(reviewer);
        return new HashMap<>();
    }

    @SneakyThrows
    @Override
    public Map<String, String> createReviewer(Reviewer created) {
        var reviewer = new Reviewer();
        reviewer.setReviewAccess(true);
        reviewerMapper.updateReviewer(created, reviewer);

        var errors = validateReviewer(reviewer);
        if (!errors.isEmpty()) {
            return errors;
        }

        var member = gitlabServiceApi.getMember(created.getUsername());
        //TODO: to mapper
        reviewer.setUsername(member.getUsername());
        reviewer.setMemberId(member.getId());
        reviewer.setAccessLevelGitLab(member.getAccessLevel());
        reviewerRepository.save(reviewer);
        return errors;
    }

    @SneakyThrows
    private Map<String, String> validateReviewer(Reviewer reviewer) {
        var errors = new HashMap<String, String>();
        if (reviewerRepository.existsByUsername(reviewer.getUsername())) {
            errors.put("username", "Ревьювер с таким именем пользователя уже существует");
        }
        if (reviewer.getId() == null) {
            var member = gitlabServiceApi.getMember(reviewer.getUsername());
            if (member == null) {
                errors.put("username", "Ревьювер с таким именем пользователя не найден в проекте");
            }
        }

        return  errors;
    }

    @Override
    public Reviewer initReviewer() {
        var result = new Reviewer();
        result.setReviewAccess(true);
        result.setReviewerNames(List.of(""));
        return result;
    }
}
