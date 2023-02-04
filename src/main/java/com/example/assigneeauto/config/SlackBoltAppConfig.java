package com.example.assigneeauto.config;

import com.example.assigneeauto.slack.SlackBlockAction;
import com.example.assigneeauto.slack.SlackCommand;
import com.slack.api.bolt.App;
import com.slack.api.bolt.AppConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SlackBoltAppConfig {

    @Bean
    public AppConfig loadSingleWorkspaceAppConfig() {
        return AppConfig.builder()
                .singleTeamBotToken(System.getenv("SLACK_BOT_TOKEN"))
                .signingSecret(System.getenv("SLACK_SIGNING_SECRET"))
                .build();
    }

    @Bean
    public App app(AppConfig appConfig, List<? extends SlackCommand> commands, List<? extends SlackBlockAction> actions) {
        var app = new App(appConfig);
        commands.forEach(c -> app.command(c.getCommandName(), c));
        actions.forEach(action -> app.blockAction(action.getActionName(), action));
        return app;
    }
}
