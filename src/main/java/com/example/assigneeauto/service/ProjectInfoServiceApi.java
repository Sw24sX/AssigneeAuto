package com.example.assigneeauto.service;

import com.example.assigneeauto.persistance.domain.ProjectInfo;

import java.util.List;
import java.util.Map;

/**
 * Сервис для обработки поддерживаемых проектов
 */
public interface ProjectInfoServiceApi {

    /**
     * Список всех проектов в сиситеме
     * @return Список всех проектов в сиситеме
     */
    List<ProjectInfo> getAll();

    /**
     * Добавление проекта в систему
     * @param info Данные о проекте
     * @return Результат сохранения
     */
    Map<String, String> create(ProjectInfo info);

    /**
     * Обновление созданного проекта
     * @param info Обновленный проект
     * @return результат обновления
     */
    Map<String, String> update(ProjectInfo info);

    /**
     * Проверить проект на доступность к автоназначению
     * @param projectId идентификатор проекта в GitLab
     * @return результат проверки, где true - проект доступен для автоназначения ревьювера
     */
    Boolean isProjectEnable(String projectId);

    /**
     * Получить проект по id в бд
     * @param id идентификатор проекта в бд
     * @return проект или null
     */
    ProjectInfo getById(Long id);

    ProjectInfo initNew();
}
