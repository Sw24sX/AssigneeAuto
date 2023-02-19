package com.example.assigneeauto.service.impl;

import com.example.assigneeauto.persistance.domain.HistoryReview;
import com.example.assigneeauto.repository.HistoryReviewRepository;
import com.example.assigneeauto.service.HistoryServiceApi;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HistoryService implements HistoryServiceApi {

    private final HistoryReviewRepository historyReviewRepository;

    @Override
    public Page<HistoryReview> getPage(Optional<Integer> page, Optional<Integer> size) {
        var currentPage = page.orElse(1);
        var pageSize = size.orElse(20);
        return historyReviewRepository.findAll(PageRequest.of(currentPage - 1, pageSize));
    }
}
