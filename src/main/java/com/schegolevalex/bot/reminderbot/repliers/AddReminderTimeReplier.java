package com.schegolevalex.bot.reminderbot.repliers;

import com.schegolevalex.bot.reminderbot.Constant;
import com.schegolevalex.bot.reminderbot.KeyboardFactory;
import com.schegolevalex.bot.reminderbot.entities.Reminder;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import java.time.format.DateTimeFormatter;
import java.util.Map;

@Component
public class AddReminderTimeReplier extends AbstractReplier {
    private final Map<Long, Reminder> tempReminders;

    @Autowired
    public AddReminderTimeReplier(TelegramWebhookBot bot, Map<Long, Reminder> tempReminders) {
        super(bot);
        this.tempReminders = tempReminders;
    }

    @SneakyThrows
    @Override
    public void reply(Long chatId, Map<String, Integer> messageIds) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(String.valueOf(chatId));
        editMessageText.setText("Текст: \"" + tempReminders.get(chatId).getText() + "\"\n" +
                "Дата: " + tempReminders.get(chatId).getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + "\"\n" +
                Constant.ADD_REMINDER_TIME_DESCRIPTION);
        editMessageText.setReplyMarkup(KeyboardFactory.withBackButton());
        editMessageText.setMessageId(messageIds.get(String.valueOf(chatId)));
        bot.execute(editMessageText);
    }
}