package com.example.assigneeauto.service;

import com.example.assigneeauto.persistance.domain.Reviewer;
import org.gitlab4j.api.GitLabApiException;

import java.util.List;

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
     * Вернуть полный список доступных для назначения ревьюверов
     * @return список ревьюверов
     */
    List<Reviewer> getAllActive();

    /**
     * Получить ревьювера по id
     * @param id идентификатор ревьювера в бд
     * @return ревьювер с заданным id или null
     */
    Reviewer getById(Long id);

    /**
     * Добавить нового ревьювера по его username.
     * Дополнительно идет запрос в GitLab с проверкой, что пользователь добавлен к проекту
     * Если пользователь не добавлен к проекту его нельзя назначить ревьювером
     * @param username имя пользователя в GitLab нового ревьювера
     * @return Новый ревьювер
     */
    Reviewer addNewReviewer(String username) throws GitLabApiException;

    /**
     * Обновить данные по ревьюверу
     * @param reviewer обновленный объект ревьювера
     * @return сохраненный ревьювер
     */
    Reviewer saveReviewer(Reviewer reviewer);

    /**
     * Удалить ревьювера
     * @param id Идентификатор ревьювера
     */
    void deleteReviewer(Long id);
}
