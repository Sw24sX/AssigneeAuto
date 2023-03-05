package com.example.assigneeauto.presets.domain;

import com.example.assigneeauto.persistance.domain.ProjectInfo;

public class ProjectInfoPreset {

    public static ProjectInfo first() {
        var result = new ProjectInfo();
        result.setId(1L);
        result.setProjectId("1");
        result.setIsAutoAssigneeEnable(true);
        result.setName("test_1");
        result.setRepositoryUrl("http://test_1");
        return result;
    }

    public static ProjectInfo second() {
        var result = new ProjectInfo();
        result.setId(2L);
        result.setProjectId("2");
        result.setIsAutoAssigneeEnable(true);
        result.setName("test_2");
        result.setRepositoryUrl("http://test_2");
        return result;
    }
}
