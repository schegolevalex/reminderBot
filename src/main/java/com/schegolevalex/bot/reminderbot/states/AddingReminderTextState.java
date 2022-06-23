package com.schegolevalex.bot.reminderbot.states;

import com.schegolevalex.bot.reminderbot.Constant;
import com.schegolevalex.bot.reminderbot.KeyboardFactory;
import com.schegolevalex.bot.reminderbot.handlers.HandlerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

@Component
public class AddingReminderTextState extends UserState {

    public AddingReminderTextState(@Lazy HandlerFactory handlerFactory) {
        super(handlerFactory);
    }

    @Override
    public BotApiMethod prepareReply(Long chatId) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(String.valueOf(chatId));
        editMessageText.setText(Constant.ADDING_REMINDER_TEXT_DESCRIPTION);
        editMessageText.setReplyMarkup(KeyboardFactory.withBackButton());
        return editMessageText;
    }
}
