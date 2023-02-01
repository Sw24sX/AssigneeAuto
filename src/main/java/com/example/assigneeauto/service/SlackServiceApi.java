package com.example.assigneeauto.service;

import com.slack.api.model.block.LayoutBlock;

import java.util.List;

/**
 * Сервис для формирования сообщений
 */
public interface SlackServiceApi {

    /**
     *
     * @param fields
     * @return
     */
    List<LayoutBlock> buildInputMessage(List<String> fields);
}
