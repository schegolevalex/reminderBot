package com.schegolevalex.bot.reminderbot.states;

import com.schegolevalex.bot.reminderbot.Constant;
import com.schegolevalex.bot.reminderbot.KeyboardFactory;
import com.schegolevalex.bot.reminderbot.entities.Reminder;
import com.schegolevalex.bot.reminderbot.handlers.HandlerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Component
public class SuccessfulAdditionState extends UserState {
    private final Map<Long, Reminder> tempReminders;

    @Autowired
    public SuccessfulAdditionState(@Lazy HandlerFactory handlerFactory,
                                   Map<Long, Reminder> tempReminders) {
        super(handlerFactory);
        this.tempReminders = tempReminders;
    }

    @Override
    public BotApiMethod getReply(Long chatId) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(String.valueOf(chatId));
        editMessageText.setText(String.format(Constant.SUCCESSFUL_ADDITION,
                tempReminders.get(chatId).getText(),
                tempReminders.get(chatId).getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                tempReminders.get(chatId).getTime()) + "\n" + Constant.CHOOSING_FIRST_ACTION_DESCRIPTION);

        editMessageText.setReplyMarkup(KeyboardFactory.withFirstActionMessage());
        return editMessageText;
    }
}
