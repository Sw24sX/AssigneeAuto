package com.example.assigneeauto.service;

import com.example.assigneeauto.persistance.domain.ProjectInfo;
import org.eclipse.jgit.blame.BlameResult;
import org.eclipse.jgit.diff.DiffEntry;

import java.util.List;

/**
 * Взаимодействие с git репозиторием
 */
public interface GitServiceApi {

    /**
     * Обновить репозиторий (git fetch + git pull)
     */
    void updateRepository(String sourceBranch, String targetBranch, ProjectInfo projectInfo);

    /**
     * Обновить вутку (git pull *branch name*)
     *
     * @param branchName Название ветки
     */
    void updateBranch(String sourceBranch, String targetBranch, ProjectInfo projectInfo);

    /**
     * Получить спосок изменений между ветками (git diff oldBranch newBranch)
     *
     * @param newBranchName Имя новой ветки, с которой будет сравниваться основная
     * @return Список изменений
     */
    List<DiffEntry> getDiffBranches(String sourceBranch, String targetBranch, ProjectInfo projectInfo);


    /**
     * Получить информацию о последнем авторе изменения каждой строки файла (git blame path/to/file)
     *
     * @param fileFromRepo Путь к файлу от корня репозитория
     * @return Результат git blame
     */
    BlameResult getBlameFile(String fileFromRepo, ProjectInfo projectInfo);

    boolean cloneRepository(String url, ProjectInfo projectInfo);
}
