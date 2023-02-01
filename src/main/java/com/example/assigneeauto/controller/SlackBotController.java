package com.example.assigneeauto.controller;

import com.example.assigneeauto.persistance.util.SlackUtil;
import com.example.assigneeauto.service.ReviewerService;
import com.slack.api.Slack;
import com.slack.api.bolt.App;
import com.slack.api.bolt.servlet.SlackAppServlet;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.methods.response.conversations.ConversationsListResponse;
import com.slack.api.model.Conversation;
import com.slack.api.model.block.LayoutBlock;
import com.slack.api.model.block.SectionBlock;
import com.slack.api.model.block.composition.MarkdownTextObject;
import com.slack.api.model.event.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.util.*;

//@RestController
//@RequestMapping("slack")
@Slf4j
@WebServlet("/slack/events")
public class SlackBotController extends SlackAppServlet {

    public SlackBotController(ReviewerService reviewerService, App app) {
        super(app);
    }

    @PostMapping("test")
    public void test(@RequestParam Map<String, Object> slackRequest) {
        var a = 1;
    }

//    @PostMapping
//    public List<LayoutBlock> getAllReviewers() throws SlackApiException, IOException {
//        var reviewers = reviewerService.getAll();
//        var result = new ArrayList<LayoutBlock>();
//        result.add(
//                SectionBlock.builder()
//                        .text(MarkdownTextObject.builder()
//                                .text("Reviewers list")
//                                .build())
//                        .build()
//        );
//        reviewers.forEach(reviewer -> {
//            var id = String.format("*Id*: %s", reviewer.getId());
//            var gitlabUsername = String.format("*Username (GitLab)*: %s", reviewer.getUsername());
//            result.add(
//                    SectionBlock.builder()
//                            .fields(Arrays.asList(
//                                    MarkdownTextObject.builder()
//                                            .text(id)
//                                            .build(),
//                                    MarkdownTextObject.builder()
//                                            .text(gitlabUsername)
//                                            .build()
//                                )
//                            )
//                            .build()
//            );
//        });
//
//        String token = "xoxb-4713318661251-4698908120103-qtzq73Mqx5Esa0UwXPfMukI8";
//        String channelName = "testchat";
//        String message = "hello";
//
//        ChatPostMessageRequest request = ChatPostMessageRequest.builder()
//                .channel(channelName)
//                .blocks(SlackUtil.buildInputMessage("Test text message", Arrays.asList("field1", "field2", "field3")))
//                .build();
////        ChatPostMessageResponse chatPostMessageResponse = postMessage(client, channelId, message);
//        sendMessage(request);
//        return result;
//    }

    private MethodsClient methodsClient = Slack.getInstance().methods("xoxb-4713318661251-4698908120103-qtzq73Mqx5Esa0UwXPfMukI8");
    private void sendMessage(ChatPostMessageRequest request) {
        try {
            // Get a response as a Java object
            ChatPostMessageResponse response = methodsClient.chatPostMessage(request);
            response.getChannel();

        } catch (IOException e) {
            log.error(e.getMessage());
        } catch (SlackApiException e) {
            log.error(e.getMessage());
        }
    }

    private String findChannel(MethodsClient client, String channelName) throws SlackApiException, IOException {
        String nextCursor = null;
        do {
            var result = getConversationsList(client, nextCursor);
            nextCursor = result.getResponseMetadata().getNextCursor();
            for (Conversation channel : result.getChannels()) {
                if (channel.getName().equalsIgnoreCase(channelName)) {
                    return channel.getId();
                }
            }
        } while (nextCursor != null);
        throw new IllegalStateException();
    }

    private ConversationsListResponse getConversationsList(MethodsClient client, String nextCursor) throws SlackApiException, IOException {
        return client.conversationsList(r -> r.cursor(nextCursor));
    }

    // https://api.slack.com/messaging/sending#publishing
//    private ChatPostMessageResponse postMessage(MethodsClient client, String conversationId, String message) throws SlackApiException, IOException {
//        return client.chatPostMessage(r -> r.channel(conversationId)..text(message));
//    }
}
