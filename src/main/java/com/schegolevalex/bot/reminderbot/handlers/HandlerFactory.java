package com.schegolevalex.bot.reminderbot.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class HandlerFactory {

    private final TextHandler textHandler;
    private final CommandHandler commandHandler;
    private final CallbackHandler callbackHandler;
    private final WrongInputHandler wrongInputHandler;

    @Autowired
    public HandlerFactory(TextHandler textHandler,
                          CommandHandler commandHandler,
                          CallbackHandler callbackHandler,
                          WrongInputHandler wrongInputHandler) {
        this.textHandler = textHandler;
        this.commandHandler = commandHandler;
        this.callbackHandler = callbackHandler;
        this.wrongInputHandler = wrongInputHandler;
    }

    public Handler getHandler(Update update) {

        if (update.hasCallbackQuery())
            return callbackHandler;

        if (update.hasMessage() && update.getMessage().isCommand())
            return commandHandler;

        if (update.hasMessage() && update.getMessage().hasText()) {
            return textHandler;
        } else return wrongInputHandler;

    }
}
