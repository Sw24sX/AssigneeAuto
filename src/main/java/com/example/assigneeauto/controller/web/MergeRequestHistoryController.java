package com.example.assigneeauto.controller.web;

import com.example.assigneeauto.persistance.mapper.HistoryMapper;
import com.example.assigneeauto.service.HistoryServiceApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("history")
@RequiredArgsConstructor
public class MergeRequestHistoryController {

    private final HistoryServiceApi historyServiceApi;
    private final HistoryMapper historyMapper;

    @GetMapping
    public ModelAndView getListHistory() {
        // TODO: 19.02.2023 add pagination
        var histories = historyServiceApi.getAll();
        var historiesDto = historyMapper.from(histories);
        var result = new ModelAndView("merge_request_history/list");
        result.addObject("histories", historiesDto);
        return result;
    }
}
