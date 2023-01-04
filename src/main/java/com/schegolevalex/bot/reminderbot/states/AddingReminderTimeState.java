package com.schegolevalex.bot.reminderbot.states;

import com.schegolevalex.bot.reminderbot.Constant;
import com.schegolevalex.bot.reminderbot.KeyboardFactory;
import com.schegolevalex.bot.reminderbot.entities.Reminder;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import java.time.format.DateTimeFormatter;
import java.util.Map;

@Component
public class AddingReminderTimeState implements UserState {
    private final Map<Long, Reminder> tempReminders;

    public AddingReminderTimeState(Map<Long, Reminder> tempReminders) {
        this.tempReminders = tempReminders;
    }

    @Override
    public BotApiMethod<?> getReply(Long chatId) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(String.valueOf(chatId));
        editMessageText.setText("Текст: \"" + tempReminders.get(chatId).getText() + "\"\n" +
                "Дата: " + tempReminders.get(chatId).getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + "\"\n" +
                Constant.ADDING_REMINDER_TIME_DESCRIPTION);
        editMessageText.setReplyMarkup(KeyboardFactory.withBackButton());
        return editMessageText;
    }
}
