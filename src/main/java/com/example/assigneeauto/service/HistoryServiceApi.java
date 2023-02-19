package com.example.assigneeauto.service;

import com.example.assigneeauto.persistance.domain.HistoryReview;

import java.util.List;

/**
 * Сервси по работе с историческими данными о назначении ревьюверов
 */
public interface HistoryServiceApi {

    List<HistoryReview> getAll();
}
