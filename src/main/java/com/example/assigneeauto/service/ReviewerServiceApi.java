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
     * @param gitUsername имя пользователя в git'е
     * @return Новый ревьювер
     */
    Reviewer addNewReviewer(String username, String gitUsername) throws GitLabApiException;

    /**
     * Снять доступ к ревью для указанного пользователя
     * @param username имя пользователя в GitLab
     * @return Измененный ревьювер
     */
    Reviewer deleteAccessReviewer(String username);

    /**
     * Обновить данные по ревьюверу
     * @param reviewer обновленный объект ревьювера
     * @return сохраненный ревьювер
     */
    Reviewer updateReviewer(Reviewer reviewer);

    /**
     * Определяет, является ли ревьювер носителем переданного имени из git репозитория
     * @param reviewer проверяемый ревьювер
     * @param name проверяемое имя
     * @return результат проверки
     */
    boolean isReviewerGitName(Reviewer reviewer, String name);
}
