package com.example.assigneeauto.controller.web;

import com.example.assigneeauto.persistance.dto.web.ReviewerDto;
import com.example.assigneeauto.persistance.mapper.ProjectInfoMapper;
import com.example.assigneeauto.persistance.mapper.ReviewerMapper;
import com.example.assigneeauto.service.ProjectInfoServiceApi;
import com.example.assigneeauto.service.ReviewerServiceApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequestMapping("reviewer")
@RequiredArgsConstructor
public class ReviewerController {
    private final ReviewerServiceApi reviewerServiceApi;
    private final ReviewerMapper reviewerMapper;
    private final ProjectInfoServiceApi projectInfoServiceApi;
    private final ProjectInfoMapper projectInfoMapper;

    @GetMapping("list")
    public ModelAndView getAll() {
        var reviewers = reviewerServiceApi.getAll();
        var result = new ModelAndView("reviewers/reviewers");
        // TODO: 05.03.2023 map to dto
        result.addObject("reviewers", reviewers);
        return result;
    }

    @PostMapping(value = "edit", params={"change"})
    public ModelAndView getById(@RequestParam(name = "change") Long id) {
        var reviewer = reviewerServiceApi.getById(id);
        var dto = reviewerMapper.from(reviewer);
        var projects = projectInfoServiceApi.getAll().stream()
                .map(projectInfoMapper::toReviewerProjectInfoDto)
                .peek(x -> x.setChecked(reviewer.getProjects().stream().anyMatch(project -> Objects.equals(project.getId(), x.id))))
                .toList();
        dto.setProjects(projects);
        var result = new ModelAndView("reviewers/edit");
        result.addObject("reviewer", dto);
        return result;
    }

    @PostMapping(value = "edit", params={"delete"})
    public String deleteReviewer(@RequestParam(name = "delete") Long id) {
        reviewerServiceApi.deleteReviewer(id);
        return "redirect:/reviewer/list";
    }

    @GetMapping("new")
    public ModelAndView createNew() {
        var reviewer = reviewerServiceApi.initReviewer();
        var projects = projectInfoServiceApi.getAll().stream()
                .map(projectInfoMapper::toReviewerProjectInfoDto)
                .toList();
        var dto = reviewerMapper.from(reviewer);
        dto.setProjects(projects);
        var result = new ModelAndView("reviewers/edit");
        result.addObject("reviewer", dto);
        return result;
    }

    @PostMapping(value = "edit", params={"save"})
    public ModelAndView save(@ModelAttribute ReviewerDto reviewer) {
        var selectedProject = projectInfoServiceApi.getAllByIds(
            reviewer.getProjects().stream()
                .filter(x -> x.checked)
                .map(x -> x.id)
                .toList()
        );
        var errors = reviewer.getId() == null ?
                reviewerServiceApi.createReviewer(reviewerMapper.to(reviewer), selectedProject) :
                reviewerServiceApi.updateReviewer(reviewerMapper.to(reviewer), selectedProject);
        if (!errors.isEmpty()) {
            var result = new ModelAndView("reviewers/edit");
            result.addObject("reviewer", reviewer);
            result.addObject("errors", errors);
            return result;
        }

        return new ModelAndView("redirect:/reviewer/list");
    }

    @PostMapping(value = "edit", params={"addRow"})
    public ModelAndView addRow(ReviewerDto reviewer) {
        reviewer.getReviewerNames().add("");
        var result = new ModelAndView("reviewers/edit");
        result.addObject("reviewer", reviewer);
        return result;
    }

    @PostMapping(value = "edit", params={"removeRow"})
    public ModelAndView removeRow(ReviewerDto reviewer, @RequestParam(name = "removeRow") int rowId) {
        reviewer.getReviewerNames().remove(rowId);
        var result = new ModelAndView("reviewers/edit");
        result.addObject("reviewer", reviewer);
        return result;
    }
}
