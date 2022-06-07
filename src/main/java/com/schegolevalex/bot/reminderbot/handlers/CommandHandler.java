package com.schegolevalex.bot.reminderbot.handlers;

import com.schegolevalex.bot.reminderbot.Constant;
import com.schegolevalex.bot.reminderbot.KeyboardFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.util.AbilityUtils;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;

@Component
public class CommandHandler implements Handler {
    private final Map<Long, UserState> userStates;

    @Autowired
    private CommandHandler(Map<Long, UserState> userStates) {
        this.userStates = userStates;
    }

    @Override
    public BotApiMethod<?> handle(Update update) {
        Long chatId = AbilityUtils.getChatId(update);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));

        switch (update.getMessage().getText()) {
            case ("/start"):
                sendMessage.setText(Constant.START_DESCRIPTION);
                sendMessage.setReplyMarkup(KeyboardFactory.withStartMessage());
                userStates.put(chatId, UserState.CHOOSING_FIRST_ACTION);
                break;

            default:
                sendMessage.setText(Constant.UNKNOWN_REQUEST);
                break;
        }

        return sendMessage;
    }
}
