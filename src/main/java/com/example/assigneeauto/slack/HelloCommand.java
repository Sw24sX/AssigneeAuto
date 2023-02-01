package com.example.assigneeauto.slack;

import com.slack.api.bolt.context.builtin.SlashCommandContext;
import com.slack.api.bolt.handler.builtin.SlashCommandHandler;
import com.slack.api.bolt.request.builtin.SlashCommandRequest;
import com.slack.api.bolt.response.Response;
import com.slack.api.methods.SlackApiException;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class HelloCommand extends AbstractSlackCommand {

    private static final String NAME = "/hello";

    @Override
    public Response apply(SlashCommandRequest slashCommandRequest, SlashCommandContext context) throws IOException, SlackApiException {
        return context.ack(r -> r.text("Thanks!"));
    }

    @Override
    public String getCommandName() {
        return NAME;
    }
}
