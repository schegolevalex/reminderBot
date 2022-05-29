package com.schegolevalex.bot.reminderbot.handlers;

import com.schegolevalex.bot.reminderbot.Constant;
import com.schegolevalex.bot.reminderbot.services.ReminderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.util.AbilityUtils;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;

@Component
public class UpdateReceiver {

    private Map<Long, UserState> userStates;
    private ReminderServiceImpl reminderService;
    private HandlerFactory handlerFactory;

    @Autowired
    private UpdateReceiver(Map<Long, UserState> userStates,
                           ReminderServiceImpl reminderService,
                           HandlerFactory handlerFactory) {
        this.userStates = userStates;
        this.reminderService = reminderService;
        this.handlerFactory = handlerFactory;
    }

    public BotApiMethod<?> receive(Update update) {
        Long chatId = AbilityUtils.getChatId(update);
        Handler handler = handlerFactory.getHandler(update);

        if (handler != null) {
            UserState state = userStates.get(chatId);
            BotApiMethod method = handler.handle(update, state);
            userStates.put(chatId, state);
            return method;
        }

        return new SendMessage(String.valueOf(chatId), Constant.UNKNOWN_REQUEST);
    }
}
