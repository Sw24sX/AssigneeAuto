package com.example.assigneeauto.repository;

import com.example.assigneeauto.persistance.domain.ProjectInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectInfoRepository extends JpaRepository<ProjectInfo, Long> {

    Boolean existsByProjectId(String projectId);
}