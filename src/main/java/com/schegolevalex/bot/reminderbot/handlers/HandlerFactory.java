package com.schegolevalex.bot.reminderbot.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;

@Component
public class HandlerFactory {

    private final Map<String, Handler> handlerMap;

    @Autowired
    public HandlerFactory(Map<String, Handler> handlerMap) {
        this.handlerMap = handlerMap;
    }

    public Handler getHandler(Update update) {

        if (update.hasCallbackQuery())
            return handlerMap.get("callbackHandler");

        if (update.hasMessage() && update.getMessage().isCommand())
            return handlerMap.get("commandHandler");

        if (update.hasMessage() && update.getMessage().hasText()) {
            return handlerMap.get("textHandler");

        } else
            return handlerMap.get("wrongInputHandler");
    }
}
