package com.example.assigneeauto.persistance.mapper;

import com.example.assigneeauto.persistance.domain.ProjectInfo;
import com.example.assigneeauto.persistance.dto.web.ProjectInfoDto;
import com.example.assigneeauto.persistance.dto.web.ProjectInfoReviewerDto;
import org.gitlab4j.api.models.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProjectInfoMapper {

    @Mapping(target = "projectId", ignore = true)
    @Mapping(target = "name", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateProjectInfo(ProjectInfo source, @MappingTarget ProjectInfo target);

    @Mapping(target = "projectId", source = "id")
    @Mapping(target = "isAutoAssigneeEnable", ignore = true)
    @Mapping(target = "id", ignore = true)
    ProjectInfo from(Project project);

    @Mapping(target = "isEnabled", source = "isAutoAssigneeEnable")
    ProjectInfoDto toDto(ProjectInfo info);

    @Mapping(target = "isAutoAssigneeEnable", source = "isEnabled")
    ProjectInfo from(ProjectInfoDto dto);

    @Mapping(target = "checked", ignore = true)
    ProjectInfoReviewerDto toReviewerProjectInfoDto(ProjectInfo info);
}
