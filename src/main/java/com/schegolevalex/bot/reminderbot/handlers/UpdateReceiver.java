package com.schegolevalex.bot.reminderbot.handlers;

import com.schegolevalex.bot.reminderbot.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.util.AbilityUtils;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class UpdateReceiver {

    private final HandlerFactory handlerFactory;

    @Autowired
    private UpdateReceiver(HandlerFactory handlerFactory) {
        this.handlerFactory = handlerFactory;
    }

    public BotApiMethod<?> receive(Update update) {
        Long chatId = AbilityUtils.getChatId(update);
        Handler handler = handlerFactory.getHandler(update);

        if (handler != null) {
            return handler.handle(update);
        } else {
            return new SendMessage(String.valueOf(chatId), Constant.UNKNOWN_REQUEST);
        }
    }
}
