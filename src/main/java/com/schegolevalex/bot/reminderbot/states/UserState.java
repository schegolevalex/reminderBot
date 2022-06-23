package com.schegolevalex.bot.reminderbot.states;

import com.schegolevalex.bot.reminderbot.handlers.Handler;
import com.schegolevalex.bot.reminderbot.handlers.HandlerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Stack;

public abstract class UserState {
    protected final HandlerFactory handlerFactory;

    @Autowired
    public UserState(@Lazy HandlerFactory handlerFactory) {
        this.handlerFactory = handlerFactory;
    }

    public abstract SendMessage setText(SendMessage sendMessage);

    public void process(Update update, Stack<UserState> userState) {
        Handler currentHandler = handlerFactory.getHandler(update);
        currentHandler.handle(update, userState);
    }
}