package com.example.assigneeauto.service;

import com.example.assigneeauto.persistance.domain.HistoryReview;
import org.springframework.data.domain.Page;

import java.util.Optional;

/**
 * Сервси по работе с историческими данными о назначении ревьюверов
 */
public interface HistoryServiceApi {

    /**
     * Получить страницу истории назначений ревьюверов
     * @param page номер страницы
     * @param size размер каждой страницы
     * @return страница
     */
    Page<HistoryReview> getPage(Optional<Integer> page, Optional<Integer> size);
}
