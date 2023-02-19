package com.example.assigneeauto.service.impl;

import com.example.assigneeauto.persistance.domain.HistoryReview;
import com.example.assigneeauto.repository.HistoryReviewRepository;
import com.example.assigneeauto.service.HistoryServiceApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryService implements HistoryServiceApi {

    private final HistoryReviewRepository historyReviewRepository;

    @Override
    public List<HistoryReview> getAll() {
        return historyReviewRepository.findAll();
    }
}
