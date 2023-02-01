package com.example.assigneeauto.config;

import com.example.assigneeauto.slack.AbstractSlackCommand;
import com.slack.api.bolt.App;
import com.slack.api.bolt.AppConfig;
import com.slack.api.bolt.handler.builtin.SlashCommandHandler;
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
    public App app(AppConfig appConfig, List<? extends AbstractSlackCommand> commands) {
        var app = new App(appConfig);
        commands.forEach(c -> app.command(c.getCommandName(), c));
//        app.command("/hello", (req, ctx) -> ctx.ack("Hi there!"));
        return app;
    }
}
