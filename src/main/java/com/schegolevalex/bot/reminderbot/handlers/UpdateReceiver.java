package com.schegolevalex.bot.reminderbot.handlers;

import com.schegolevalex.bot.reminderbot.Constant;
import com.schegolevalex.bot.reminderbot.entities.Reminder;
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

    private Map<Long, UserState> statesDB;
    private ReminderServiceImpl reminderService;
    private HandlerFactory handlerFactory;

    @Autowired
    private UpdateReceiver(Map<Long, UserState> statesDB,
                           ReminderServiceImpl reminderService, HandlerFactory handlerFactory) {
        this.statesDB = statesDB;
        this.reminderService = reminderService;
        this.handlerFactory = handlerFactory;
    }

    public BotApiMethod<?> receive(Update update) {
        Long chatId = AbilityUtils.getChatId(update);
        Handler handler = handlerFactory.getHandler(update);

        if (handler != null) {
            return handler.handle(update);
        }

        return new SendMessage(String.valueOf(update.getMessage().getChatId()), Constant.UNKNOWN_REQUEST);
    }
}
