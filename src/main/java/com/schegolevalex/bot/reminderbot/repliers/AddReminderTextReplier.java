package com.schegolevalex.bot.reminderbot.repliers;

import com.schegolevalex.bot.reminderbot.Constant;
import com.schegolevalex.bot.reminderbot.KeyboardFactory;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import java.util.Map;

@Component
public class AddReminderTextReplier extends AbstractReplier {

    public AddReminderTextReplier(TelegramWebhookBot bot) {
        super(bot);
    }

    @SneakyThrows
    @Override
    public void reply(Long chatId, Map<String, Integer> messageIds) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(String.valueOf(chatId));
        editMessageText.setText(Constant.ADD_REMINDER_TEXT_DESCRIPTION);
        editMessageText.setReplyMarkup(KeyboardFactory.withBackButton());
        editMessageText.setMessageId(messageIds.get(String.valueOf(chatId)));
        bot.execute(editMessageText);
    }
}
