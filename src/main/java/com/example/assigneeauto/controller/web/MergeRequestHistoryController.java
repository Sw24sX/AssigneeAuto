package com.example.assigneeauto.controller.web;

import com.example.assigneeauto.persistance.mapper.HistoryMapper;
import com.example.assigneeauto.service.HistoryServiceApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("history")
@RequiredArgsConstructor
public class MergeRequestHistoryController {

    private final HistoryServiceApi historyServiceApi;
    private final HistoryMapper historyMapper;

    @GetMapping
    public ModelAndView getListHistory(Model model, @RequestParam("page") Optional<Integer> page,
                                       @RequestParam("size") Optional<Integer> size) {
        var pageHistory = historyServiceApi.getPage(page, size);
        var historiesDto = historyMapper.from(pageHistory );
        var pageNumbers = IntStream.rangeClosed(1, pageHistory.getTotalPages())
                .boxed()
                .collect(Collectors.toList());

        var result = new ModelAndView("merge_request_history/list");
        result.addObject("histories", historiesDto);
        result.addObject("pageHistory", pageHistory);
        result.addObject("currentPage", page.orElse(1));
        model.addAttribute("pageNumbers", pageNumbers);
        return result;
    }
}
