package com.example.assigneeauto.controller.web;

import com.example.assigneeauto.persistance.domain.Reviewer;
import com.example.assigneeauto.persistance.domain.ReviewerName;
import com.example.assigneeauto.persistance.mapper.ReviewerMapper;
import com.example.assigneeauto.service.ReviewerServiceApi;
import lombok.RequiredArgsConstructor;
import org.gitlab4j.api.GitLabApiException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;

@Controller
@RequestMapping("reviewer")
@RequiredArgsConstructor
public class ReviewerController {
    private final ReviewerServiceApi reviewerServiceApi;
    private final ReviewerMapper reviewerMapper;

    @GetMapping("list")
    public ModelAndView getAll() {
        var reviewers = reviewerServiceApi.getAll();
        var result = new ModelAndView("reviewers/reviewers");
        result.addObject("reviewers", reviewers);
        return result;
    }

    @GetMapping("{id}")
    public ModelAndView getById(@PathVariable("id") Long id) {
        var reviewer = reviewerServiceApi.getById(id);
//        var dto = reviewerMapper.from(reviewer);
        var result = new ModelAndView("reviewers/edit");
        result.addObject("reviewer", reviewer);
        return result;
    }

    @GetMapping("{id}/delete")
    public String deleteReviewer(@PathVariable Long id) {
        reviewerServiceApi.deleteReviewer(id);
        return "redirect:/reviewer/list";
    }

    @GetMapping("new")
    public ModelAndView createNew() {
        var result = new ModelAndView("reviewers/edit");
        result.addObject("reviewer", new Reviewer());
        return result;
    }

    @PostMapping(value = "edit", params={"save"})
    public String save(@ModelAttribute Reviewer reviewer) throws GitLabApiException {
        var updatedReviewer = reviewer;
        if (reviewer.getId() == null) {
            updatedReviewer = reviewerServiceApi.addNewReviewer(reviewer.getUsername());
            reviewerMapper.updateReviewer(reviewer, updatedReviewer);
        }
        reviewerServiceApi.saveReviewer(updatedReviewer);
        //todo: проверка на ошибки + сохранение списка имен + обработка exceptions
        return "redirect:/reviewer/list";
    }

    @PostMapping(value = "edit", params={"addRow"})
    public ModelAndView addRow(Reviewer reviewer) {
        var newName = new ReviewerName();
        newName.setReviewer(reviewer);
        if (reviewer.getReviewerNames() == null) {
            reviewer.setReviewerNames(new ArrayList<>());
        }
        reviewer.getReviewerNames().add(newName);

        var result = new ModelAndView("reviewers/edit");
        result.addObject("reviewer", reviewer);
        return result;
    }

    @PostMapping(value = "edit", params={"removeRow"})
    public ModelAndView removeRow(Reviewer reviewer) {
        var removeRow = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest()
                .getParameter("removeRow");
        var rowId = Integer.parseInt(removeRow);
        reviewer.getReviewerNames().remove(rowId);

        var result = new ModelAndView("reviewers/edit");
        result.addObject("reviewer", reviewer);
        return result;
    }
}
