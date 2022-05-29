package com.schegolevalex.bot.reminderbot.handlers;

import com.schegolevalex.bot.reminderbot.Constant;
import com.schegolevalex.bot.reminderbot.KeyboardFactory;
import com.schegolevalex.bot.reminderbot.entities.Reminder;
import com.schegolevalex.bot.reminderbot.services.ReminderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.sql.Timestamp;
import java.util.Map;

@Component
public class UpdateReceiver {

    private Map<Long, UserState> statesDB;
    private ReminderServiceImpl reminderService;
    private CallbackHandler callbackHandler;
    private CommandHandler commandHandler;
    private TextHandler textHandler;

    @Autowired
    private UpdateReceiver(Map<Long, UserState> statesDB, ReminderServiceImpl reminderService, CallbackHandler callbackHandler, CommandHandler commandHandler, TextHandler textHandler) {
        this.statesDB = statesDB; // возможно в будущем можно будет отказаться от внедрения этого объекта спрингом, при условии, что мапа состояний не понадобиться нигде дальше
        this.reminderService = reminderService;
        this.callbackHandler = callbackHandler;
        this.commandHandler = commandHandler;
        this.textHandler = textHandler;
    }

    public BotApiMethod<?> receive(Update update) {
        if (update.hasCallbackQuery())
            return callbackHandler.handle(update);

        if (update.hasMessage() && update.getMessage().isCommand())
            return commandHandler.handle(update);

        if (update.hasMessage() && update.getMessage().hasText())
            return textHandler.handle(update);

        return new SendMessage(String.valueOf(update.getMessage().getChatId()), Constant.UNKNOWN_REQUEST);
    }
}
