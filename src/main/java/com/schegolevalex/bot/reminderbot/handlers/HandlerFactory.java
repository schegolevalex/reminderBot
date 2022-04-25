package com.schegolevalex.bot.reminderbot.handlers;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class HandlerFactory {

    public Handler getHandler(Update update) {

        if (update.hasCallbackQuery())
            return CallbackHandler.getInstance();

        if (update.hasMessage() && update.getMessage().isCommand())
            return CommandHandler.getInstance();

        if (update.hasMessage() && update.getMessage().hasText()) {
            return TextHandler.getInstance();
        } else return null;

    }
}
