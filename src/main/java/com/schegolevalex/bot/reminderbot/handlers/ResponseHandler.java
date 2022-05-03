package com.schegolevalex.bot.reminderbot.handlers;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;


@Component
public class ResponseHandler {


    public BotApiMethod<?> onUpdateReceiver(Update update) {
        if (update.hasCallbackQuery()) {
            return handleMessageWithCallback(update);
        }
        if (update.getMessage().getText().startsWith("/")) {
            return handleMessageWithCommand(update);
        }
        if (update.getMessage().hasText()) {
            return handleMessageWithText(update);
        }

        return new SendMessage(String.valueOf(update.getMessage().getChatId()), "Не понимаю, о чем Вы. Попробуйте снова.");

    }

    private BotApiMethod<?> handleMessageWithText(Update update) {
        return null;
    }

    private BotApiMethod<?> handleMessageWithCommand(Update update) {
        return null;
    }

    private BotApiMethod<?> handleMessageWithCallback(Update update) {
        return null;
    }


}
