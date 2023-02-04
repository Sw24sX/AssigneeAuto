package com.example.assigneeauto.slack.action;

import com.example.assigneeauto.slack.SlackBlockAction;
import com.google.gson.Gson;
import com.slack.api.bolt.context.builtin.ActionContext;
import com.slack.api.bolt.request.builtin.BlockActionRequest;
import com.slack.api.bolt.response.Response;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.response.views.ViewsUpdateResponse;
import com.slack.api.model.view.View;
import com.slack.api.util.json.GsonFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

import static com.slack.api.model.block.Blocks.*;
import static com.slack.api.model.block.composition.BlockCompositions.markdownText;
import static com.slack.api.model.block.composition.BlockCompositions.plainText;
import static com.slack.api.model.block.element.BlockElements.plainTextInput;
import static com.slack.api.model.view.Views.*;

@Component
public class PingAgainAction implements SlackBlockAction {
    @Override
    public String getActionName() {
        return "ping-again";
    }

    @Override
    public Response apply(BlockActionRequest blockActionRequest, ActionContext context) throws IOException, SlackApiException {
        String value = blockActionRequest.getPayload().getActions().get(0).getValue(); // "button's value"
        if (blockActionRequest.getPayload().getResponseUrl() != null) {
            // Post a message to the same channel if it's a block in a message
            context.respond("You've sent " + value + " by clicking the button!");
        }

        String categoryId = blockActionRequest.getPayload().getActions().get(0).getSelectedOption().getValue();
        View currentView = blockActionRequest.getPayload().getView();
        String privateMetadata = currentView.getPrivateMetadata();
        View viewForTheCategory = buildViewByCategory(categoryId, privateMetadata);
        ViewsUpdateResponse viewsUpdateResp = context.client().viewsUpdate(r -> r
                .viewId(currentView.getId())
                .hash(currentView.getHash())
                .view(viewForTheCategory)
        );
        return context.ack();
    }

    View buildViewByCategory(String categoryId, String privateMetadata) {
        Gson gson = GsonFactory.createSnakeCase();
        Map<String, String> metadata = gson.fromJson(privateMetadata, Map.class);
        metadata.put("categoryId", categoryId);
        String updatedPrivateMetadata = gson.toJson(metadata);
        return view(view -> view
                .callbackId("meeting-arrangement")
                .type("modal")
                .notifyOnClose(true)
                .title(viewTitle(title -> title.type("plain_text").text("Meeting Arrangement").emoji(true)))
                .submit(viewSubmit(submit -> submit.type("plain_text").text("Submit").emoji(true)))
                .close(viewClose(close -> close.type("plain_text").text("Cancel").emoji(true)))
                .privateMetadata(updatedPrivateMetadata)
                .blocks(asBlocks(
                        section(section -> section.blockId("category-block").text(markdownText("You've selected \"" + categoryId + "\""))),
                        input(input -> input
                                .blockId("agenda-block")
                                .element(plainTextInput(pti -> pti.actionId("agenda-action").multiline(true)))
                                .label(plainText(pt -> pt.text("Detailed Agenda").emoji(true)))
                        )
                ))
        );
    }
}
