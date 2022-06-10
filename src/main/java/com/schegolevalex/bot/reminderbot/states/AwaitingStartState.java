package com.schegolevalex.bot.reminderbot.states;

import com.schegolevalex.bot.reminderbot.Constant;
import com.schegolevalex.bot.reminderbot.KeyboardFactory;
import com.schegolevalex.bot.reminderbot.handlers.HandlerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public class AwaitingStartState extends UserState {

    @Autowired
    public AwaitingStartState(@Lazy ReminderFacade reminderFacade,
                              @Lazy HandlerFactory handlerFactory) {
        super(reminderFacade, handlerFactory);
    }

    @Override
    public SendMessage setText(SendMessage sendMessage) {
        sendMessage.setText(Constant.AWAITING_START_DESCRIPTION);
        sendMessage.setReplyMarkup(KeyboardFactory.withStartMessage());
        return sendMessage;
    }
}