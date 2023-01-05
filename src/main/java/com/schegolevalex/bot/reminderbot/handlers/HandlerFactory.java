package com.schegolevalex.bot.reminderbot.handlers;

import com.schegolevalex.bot.reminderbot.states.UserState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;
import java.util.Stack;

@Component
public class HandlerFactory {

    private final Map<String, Handler> handlerMap;

    @Autowired
    public HandlerFactory(Map<String, Handler> handlerMap) {
        this.handlerMap = handlerMap;
    }

    public void handle(Update update, Stack<UserState> userStateStack) {

        if (update.hasCallbackQuery())
            handlerMap.get("callbackHandler").handle(update, userStateStack);

        else if (update.hasMessage() && update.getMessage().isCommand())
            handlerMap.get("commandHandler").handle(update, userStateStack);

        else if (update.hasMessage() && update.getMessage().hasText())
            handlerMap.get("textHandler").handle(update, userStateStack);

        else if (userStateStack.peek().getClass().getSimpleName().equals("AddingReminderDateState"))
            handlerMap.get("wrongInputDateHandler").handle(update, userStateStack);

        else if (userStateStack.peek().getClass().getSimpleName().equals("AddingReminderTimeState"))
            handlerMap.get("wrongInputTimeHandler").handle(update, userStateStack);

        else handlerMap.get("wrongInputCommonHandler").handle(update, userStateStack);
    }
}
