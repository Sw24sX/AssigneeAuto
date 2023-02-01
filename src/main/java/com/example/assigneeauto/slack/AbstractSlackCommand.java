package com.example.assigneeauto.slack;

import com.slack.api.bolt.handler.builtin.SlashCommandHandler;

public abstract class AbstractSlackCommand implements SlashCommandHandler {
    public abstract String getCommandName();
}
