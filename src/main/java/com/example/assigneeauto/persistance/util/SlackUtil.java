package com.example.assigneeauto.persistance.util;

import com.slack.api.model.block.ActionsBlock;
import com.slack.api.model.block.InputBlock;
import com.slack.api.model.block.LayoutBlock;
import com.slack.api.model.block.SectionBlock;
import com.slack.api.model.block.composition.MarkdownTextObject;
import com.slack.api.model.block.composition.PlainTextObject;
import com.slack.api.model.block.element.ButtonElement;
import com.slack.api.model.block.element.PlainTextInputElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Набор методов для функционирования бота Slack
 */
public class SlackUtil {

    /**
     * Формирует сообщение с набором полей для ввода
     * @param fields Список названий полей для ввода
     * @return список блоков сообщения + кнопка подтверждения
     */
    public static List<LayoutBlock> buildInputMessage(String message, List<String> fields) {
        var result = new ArrayList<LayoutBlock>();
        result.add(
                SectionBlock.builder()
                        .text(MarkdownTextObject.builder()
                                .text(message)
                                .build())
                        .build()
        );
        fields.forEach(field -> result.add(
                InputBlock.builder()
                        .label(PlainTextObject.builder()
                                .text(field)
                                .build())
                        .element(PlainTextInputElement.builder()
                                .multiline(false)
                                .build())
                        .build()
        ));
        result.add(
                ActionsBlock.builder()
                        .elements(Arrays.asList(
                                ButtonElement.builder()
                                        .text(PlainTextObject.builder()
                                                .text("Отправить")
                                                .build())
//                                        .url("/slack/test")
                                        .build()
                        ))
                        .build()
        );
        return result;
    }
}
