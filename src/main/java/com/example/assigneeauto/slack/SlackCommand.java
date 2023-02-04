package com.example.assigneeauto.slack;

import com.slack.api.bolt.handler.builtin.SlashCommandHandler;

public interface SlackCommand extends SlashCommandHandler {
    String getCommandName();
}
