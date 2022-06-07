package com.schegolevalex.bot.reminderbot.handlers;

import com.schegolevalex.bot.reminderbot.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.util.AbilityUtils;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;
import java.util.Stack;

@Component
public class UpdateReceiver {

    private final HandlerFactory handlerFactory;

    @Autowired
    private UpdateReceiver(HandlerFactory handlerFactory) {
        this.handlerFactory = handlerFactory;
    }

    public BotApiMethod<?> receive(Update update, Map<Long, Stack<UserState>> userStates) {
        Long chatId = AbilityUtils.getChatId(update);
        Handler handler = handlerFactory.getHandler(update);

        Stack<UserState> userState;

        if (handler != null) {
            if (userStates.get(chatId) != null) {
                userState = userStates.get(chatId);
            } else {
                userState = new Stack<>();
                userStates.put(chatId, userState);
            }
            return handler.handle(update, userState);
        } else {
            return new SendMessage(String.valueOf(chatId), Constant.UNKNOWN_REQUEST);
        }
    }
}
