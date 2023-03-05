package com.example.assigneeauto.controller.web;

import com.example.assigneeauto.persistance.dto.web.ProjectInfoDto;
import com.example.assigneeauto.persistance.mapper.ProjectInfoMapper;
import com.example.assigneeauto.service.ProjectInfoServiceApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.stream.Collectors;

@Controller
@RequestMapping("project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectInfoServiceApi projectInfoServiceApi;
    private final ProjectInfoMapper projectInfoMapper;

    @GetMapping("list")
    public ModelAndView getAll() {
        var projects = projectInfoServiceApi.getAll();
        var result = new ModelAndView("project/list");
        result.addObject("projects", projects.stream().map(projectInfoMapper::toDto).collect(Collectors.toList()));
        return result;
    }

    @GetMapping("new")
    public ModelAndView create() {
        var createdProject = projectInfoServiceApi.initNew();
        var result = new ModelAndView("project/edit");
        result.addObject("project", projectInfoMapper.toDto(createdProject));
        return result;
    }

    @PostMapping(value = "edit", params = {"change"})
    public ModelAndView changeById(@RequestParam(name = "change") Long id) {
        var project = projectInfoServiceApi.getById(id);
        var result = new ModelAndView("project/edit");
        result.addObject("project", projectInfoMapper.toDto(project));
        return result;
    }

    @PostMapping(value = "edit", params = {"save"})
    public ModelAndView save(@ModelAttribute ProjectInfoDto project) {
        var errors = project.getId() == null ?
                projectInfoServiceApi.create(projectInfoMapper.from(project)) :
                projectInfoServiceApi.update(projectInfoMapper.from(project));
        if (!errors.isEmpty()) {
            var result = new ModelAndView("project/edit");
            result.addObject("project", project);
            result.addObject("errors", errors);
            return result;
        }

        return new ModelAndView("redirect:/project/list");
    }
}
