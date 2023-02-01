package com.example.assigneeauto.slack;

import com.example.assigneeauto.persistance.util.SlackUtil;
import com.slack.api.app_backend.slash_commands.response.SlashCommandResponse;
import com.slack.api.bolt.context.builtin.SlashCommandContext;
import com.slack.api.bolt.handler.builtin.SlashCommandHandler;
import com.slack.api.bolt.request.builtin.SlashCommandRequest;
import com.slack.api.bolt.response.Response;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.SlackApiResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;

import static com.slack.api.model.block.Blocks.*;
import static com.slack.api.model.block.composition.BlockCompositions.markdownText;
import static com.slack.api.model.block.composition.BlockCompositions.plainText;
import static com.slack.api.model.block.element.BlockElements.asElements;
import static com.slack.api.model.block.element.BlockElements.button;


@Component
public class HelloCommand extends AbstractSlackCommand {

    private static final String NAME = "/ping";

    @Override
    public Response apply(SlashCommandRequest slashCommandRequest, SlashCommandContext context) throws IOException, SlackApiException {
        var response = new SlashCommandResponse();
//        response.setBlocks(SlackUtil.buildInputMessage("test", Arrays.asList("test_1", "test_2")));
//        return context.ack(r -> r.text("Thanks!"));
//        return context.ack(response);
        return context.ack(asBlocks(
                section(section -> section.text(markdownText(":wave: pong"))),
                actions(actions -> actions
                        .elements(asElements(
                                button(b -> b.actionId("ping-again").text(plainText(pt -> pt.text("Ping"))).value("ping"))
                        ))
                )
        ));
    }

    @Override
    public String getCommandName() {
        return NAME;
    }
}
