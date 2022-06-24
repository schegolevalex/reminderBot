package com.schegolevalex.bot.reminderbot.states;

import com.schegolevalex.bot.reminderbot.Constant;
import com.schegolevalex.bot.reminderbot.KeyboardFactory;
import com.schegolevalex.bot.reminderbot.entities.Reminder;
import com.schegolevalex.bot.reminderbot.handlers.HandlerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import java.util.Map;

@Component
public class AddingReminderDateState extends UserState {
    private final Map<Long, Reminder> tempReminders;


    public AddingReminderDateState(@Lazy HandlerFactory handlerFactory,
                                   Map<Long, Reminder> tempReminders) {
        super(handlerFactory);
        this.tempReminders = tempReminders;
    }

    @Override
    public BotApiMethod getReply(Long chatId) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(String.valueOf(chatId));
        editMessageText.setText("Текст: \"" + tempReminders.get(chatId).getText() +
                "\"\n" + Constant.ADDING_REMINDER_DATE_DESCRIPTION);
        editMessageText.setReplyMarkup(KeyboardFactory.withBackButton());
        return editMessageText;
    }
}
