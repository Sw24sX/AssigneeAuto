package com.example.assigneeauto.persistance.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProjectInfo extends BaseEntity {

    private String projectId;

    private String name;

    @Column(name = "auto_assignee_enable")
    private Boolean isAutoAssigneeEnable;
}
