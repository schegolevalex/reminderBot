package com.schegolevalex.bot.reminderbot.repliers;

import com.schegolevalex.bot.reminderbot.Constant;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Map;

@Component
public class WrongInputDateReplier extends AbstractReplier {

    public WrongInputDateReplier(TelegramWebhookBot bot) {
        super(bot);
    }

    @Override
    public void reply(Long chatId, Map<String, Integer> messageIds) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(String.valueOf(chatId));
        editMessageText.setText(Constant.WRONG_DATE_FORMAT);
        editMessageText.setMessageId(messageIds.get(String.valueOf(chatId)));
        try {
            bot.execute(editMessageText);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
