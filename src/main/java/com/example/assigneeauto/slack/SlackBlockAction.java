package com.example.assigneeauto.slack;

import com.slack.api.bolt.handler.builtin.BlockActionHandler;
import com.slack.api.bolt.handler.builtin.SlashCommandHandler;

public interface SlackBlockAction extends BlockActionHandler {
    String getActionName();
}
