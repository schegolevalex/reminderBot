package com.schegolevalex.bot.reminderbot.handlers;

import com.schegolevalex.bot.reminderbot.Constant;
import com.schegolevalex.bot.reminderbot.KeyboardFactory;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.util.AbilityUtils;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Stack;

@Component
public class CommandHandler implements Handler {

    @Override
    public BotApiMethod<?> handle(Update update, Stack<UserState> userState) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(AbilityUtils.getChatId(update)));

        switch (update.getMessage().getText()) {
            case ("/start"):
                sendMessage.setText(Constant.START_DESCRIPTION);
                sendMessage.setReplyMarkup(KeyboardFactory.withStartMessage());
                userState.push(UserState.CHOOSING_FIRST_ACTION);
                break;

            default:
                sendMessage.setText(Constant.UNKNOWN_REQUEST);
                break;
        }

        return sendMessage;
    }
}
