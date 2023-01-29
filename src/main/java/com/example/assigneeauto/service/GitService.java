package com.example.assigneeauto.service;

import org.eclipse.jgit.blame.BlameResult;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.Ref;

import java.util.List;

/**
 * Взаимодействие с git репозиторием
 */
public interface GitService {

    /**
     * Обновить репозиторий (git fetch + git pull)
     */
    void updateRepository();

    /**
     * Обновить вутку (git pull *branch name*)
     *
     * @param branchName Название ветки
     */
    void updateBranch(String branchName);

    /**
     * Получить спосок изменений между ветками (git diff oldBranch newBranch)
     *
     * @param newBranchName Имя новой ветки, с которой будет сравниваться основная
     * @return Список изменений
     */
    List<DiffEntry> getDiffBranches(String newBranchName);

    /**
     * Список всех веток репозитория
     *
     * @return Список всех веток репозитория
     */
    List<Ref> getAllBranches();

    /**
     * Получить информацию о последнем авторе изменения каждой строки файла (git blame path/to/file)
     *
     * @param fileFromRepo Путь к файлу от корня репозитория
     * @return Результат git blame
     */
    BlameResult getBlameFile(String fileFromRepo);
}
