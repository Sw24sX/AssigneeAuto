package com.example.assigneeauto.persistance.dto.web;

import lombok.Data;

@Data
public class ProjectInfoDto {
    private Long id;
    private String projectId;
    private String name;
    private Boolean isEnabled;
}
