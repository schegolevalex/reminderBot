package com.schegolevalex.bot.reminderbot.states;

import com.schegolevalex.bot.reminderbot.handlers.Handler;
import com.schegolevalex.bot.reminderbot.handlers.HandlerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Stack;

public abstract class UserState {
    // возможно стоит удалить это поле
    protected final ReminderFacade reminderFacade;
    protected final HandlerFactory handlerFactory;

    @Autowired
    public UserState(ReminderFacade reminderFacade,
                     HandlerFactory handlerFactory) {
        this.reminderFacade = reminderFacade;
        this.handlerFactory = handlerFactory;
    }

    abstract SendMessage setText(SendMessage sendMessage);

    void handle(Update update, Stack<UserState> userState) {
        Handler currentHandler = handlerFactory.getHandler(update);
        currentHandler.handle(update, userState);
    }
}
