package com.example.assigneeauto.service;

import com.example.assigneeauto.persistance.domain.Reviewer;

import java.util.List;
import java.util.Map;

/**
 * Сервис для управления списокм ревьюверов
 */
public interface ReviewerServiceApi {
    /**
     * Вернуть полный список ревьюверов
     * @return список ревьюверов
     */
    List<Reviewer> getAll();

    /**
     * Вернуть полный список доступных для назначения ревьюверов в заданном проекте
     * @param projectId Идентификатор проекта в GitLab
     * @return список доступных для назначения ревьюверов
     */
    List<Reviewer> getAllActive(String projectId);

    /**
     * Получить ревьювера по id
     * @param id идентификатор ревьювера в бд
     * @return ревьювер с заданным id или null
     */
    Reviewer getById(Long id);

    /**
     * Удалить ревьювера
     * @param id Идентификатор ревьювера
     */
    void deleteReviewer(Long id);

    /**
     * Обновление или создание ревьювера
     * @param updated Новые данные о ревьювере
     * @return Список ошибок при обновлении
     */
    Map<String, String> updateReviewer(Reviewer updated);

    Map<String, String> createReviewer(Reviewer created);

    /**
     * Создает и заполняет начальными данными нового ревьювера
     * @return новый ревьювер
     */
    Reviewer initReviewer();
}
