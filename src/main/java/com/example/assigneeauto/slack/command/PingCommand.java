package com.example.assigneeauto.slack.command;

import com.example.assigneeauto.slack.SlackCommand;
import com.example.assigneeauto.slack.action.PingAgainAction;
import com.slack.api.app_backend.slash_commands.response.SlashCommandResponse;
import com.slack.api.bolt.context.builtin.SlashCommandContext;
import com.slack.api.bolt.request.builtin.SlashCommandRequest;
import com.slack.api.bolt.response.Response;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.response.views.ViewsOpenResponse;
import com.slack.api.model.view.View;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.slack.api.model.block.Blocks.*;
import static com.slack.api.model.block.composition.BlockCompositions.*;
import static com.slack.api.model.block.element.BlockElements.*;
import static com.slack.api.model.view.Views.*;


@Component
@RequiredArgsConstructor
public class PingCommand implements SlackCommand {

    private static final String NAME = "/ping";
    private final PingAgainAction pingAgainAction;

    @Override
    public Response apply(SlashCommandRequest slashCommandRequest, SlashCommandContext context) throws IOException, SlackApiException {
        var response = new SlashCommandResponse();
//        response.setBlocks(SlackUtil.buildInputMessage("test", Arrays.asList("test_1", "test_2")));
//        return context.ack(r -> r.text("Thanks!"));
//        return context.ack(response);

//        return context.ack(asBlocks(
//                section(section -> section.text(markdownText(":wave: pong"))),
//                input(input -> input
//                        .blockId("agenda-block")
//                        .optional(false)
//                        .element(plainTextInput(pti -> pti.actionId("agenda-action").multiline(false)))
//                        .label(plainText(pt -> pt.text("Detailed Agenda").emoji(true)))
//                ),
//                actions(actions -> actions
//
//                        .elements(asElements(
//                                button(b -> b
//                                        .actionId(pingAgainAction.getActionName())
//                                        .text(plainText(pt -> pt.text("Ping")))
//                                        .value("ping"))
//                        ))
//                )
//        ));

//        context.asyncClient().viewsOpen(r -> r
//                .triggerId(slashCommandRequest.getContext().getTriggerId())
//                .view(view(v -> v
//                        .type("modal")
//                        .callbackId("test-view")
//                        .title(viewTitle(vt -> vt.type("plain_text").text("Modal by Global Shortcut")))
//                        .close(viewClose(vc -> vc.type("plain_text").text("Close")))
//                        .submit(viewSubmit(vs -> vs.type("plain_text").text("Submit")))
//                        .blocks(asBlocks(input(input -> input
//                                .blockId("agenda-block")
//                                .optional(false)
//                                .element(plainTextInput(pti -> pti.actionId(pingAgainAction.getActionName())))
//                                .label(plainText(pt -> pt.text("Detailed Agenda").emoji(true)))
//                        )))
//                )));

        ViewsOpenResponse viewsOpenRes = context.client().viewsOpen(r -> r
                .triggerId(context.getTriggerId())
                .view(buildView()));
        if (viewsOpenRes.isOk()) return context.ack();
        else return Response.builder().statusCode(500).body(viewsOpenRes.getError()).build();
    }

    View buildView() {
        return view(view -> view
                .callbackId("meeting-arrangement")
                .type("modal")
                .notifyOnClose(true)
                .title(viewTitle(title -> title.type("plain_text").text("Meeting Arrangement").emoji(true)))
                .submit(viewSubmit(submit -> submit.type("plain_text").text("Submit").emoji(true)))
                .close(viewClose(close -> close.type("plain_text").text("Cancel").emoji(true)))
                .privateMetadata("{\"response_url\":\"https://hooks.slack.com/actions/T1ABCD2E12/330361579271/0dAEyLY19ofpLwxqozy3firz\"}")
                .blocks(asBlocks(
                        section(section -> section
                                .blockId("category-block")
                                .text(markdownText("Select a category of the meeting!"))
                                .accessory(staticSelect(staticSelect -> staticSelect
                                        .actionId(pingAgainAction.getActionName())
                                        .placeholder(plainText("Select a category"))
                                        .options(asOptions(
                                                option(plainText("Customer"), "customer"),
                                                option(plainText("Partner"), "partner"),
                                                option(plainText("Internal"), "internal")
                                        ))
                                ))
                        ),
                        input(input -> input
                                .blockId("agenda-block")
                                .element(plainTextInput(pti -> pti.actionId("agenda-action").multiline(true)))
                                .label(plainText(pt -> pt.text("Detailed Agenda").emoji(true)))
                        )
                ))
        );
    }

    @Override
    public String getCommandName() {
        return NAME;
    }
}
