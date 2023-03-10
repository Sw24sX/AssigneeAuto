package com.example.assigneeauto.service.impl;

import com.example.assigneeauto.persistance.domain.ProjectInfo;
import com.example.assigneeauto.persistance.mapper.ProjectInfoMapper;
import com.example.assigneeauto.repository.ProjectInfoRepository;
import com.example.assigneeauto.service.GitServiceApi;
import com.example.assigneeauto.service.GitlabServiceApi;
import com.example.assigneeauto.service.ProjectInfoServiceApi;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProjectInfoService implements ProjectInfoServiceApi {

    private final ProjectInfoRepository projectInfoRepository;
    private final GitlabServiceApi gitlabServiceApi;
    private final ProjectInfoMapper projectInfoMapper;
    private final GitServiceApi gitServiceApi;

    @Override
    public List<ProjectInfo> getAll() {
        return projectInfoRepository.findAll();
    }

    @SneakyThrows
    @Override
    public Map<String, String> create(ProjectInfo info) {
        if (projectInfoRepository.existsByProjectId(info.getProjectId())) {
            return Map.of("projectId", "Проект с заданным ProjectId уже задан в системе");
        }
        var project = gitlabServiceApi.getProject(info.getProjectId());
        var createdProject = projectInfoMapper.from(project);
        projectInfoMapper.updateProjectInfo(info, createdProject);
        if (!gitServiceApi.cloneRepository(info.getRepositoryUrl(), createdProject)) {
            return Map.of("repositoryUrl", "Не удалось клонировать репозиторий");
        }
        projectInfoRepository.save(createdProject);
        return Map.of();
    }

    @Override
    public Map<String, String> update(ProjectInfo info) {
        var projectInfo = projectInfoRepository.findById(info.getId()).orElse(null);
        if (projectInfo == null) {
            return Map.of("id", "Проект с заданным id не найден в системе");
        }
        projectInfoMapper.updateProjectInfo(info, projectInfo);
        projectInfoRepository.save(projectInfo);
        return Map.of();
    }

    @Override
    public Boolean isProjectEnable(String projectId) {
        return projectInfoRepository.existsByProjectId(projectId);
    }

    @Override
    public ProjectInfo getById(Long id) {
        return projectInfoRepository.findById(id).orElse(null);
    }

    @Override
    public ProjectInfo initNew() {
        var result = new ProjectInfo();
        result.setIsAutoAssigneeEnable(true);
        return result;
    }

    @Override
    public List<ProjectInfo> getAllByIds(List<Long> ids) {
        return projectInfoRepository.findAllById(ids);
    }

    @Override
    public ProjectInfo getByProjectId(String projectId) {
        return projectInfoRepository.findByProjectId(projectId);
    }
}
