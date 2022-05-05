package com.schegolevalex.bot.reminderbot.handlers;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;


@Component
public class ResponseHandler {


    public BotApiMethod<?> onUpdateReceiver(Update update) {
        if (update.hasCallbackQuery()) {
            return handleMessageWithCallback(update);
        }
        if (update.getMessage().hasEntities() &&
                update.getMessage().getEntities().stream()
                        .anyMatch(entity -> entity.getType().equals("bot_command"))) {
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
        if (update.getMessage().getText().equals("/start")) {
            SendMessage sendMessage = new SendMessage(String.valueOf(update.getMessage().getChatId()), "Привет!");
            return sendMessage;
        }


        return null;
    }

    private BotApiMethod<?> handleMessageWithCallback(Update update) {
        return null;
    }


}
