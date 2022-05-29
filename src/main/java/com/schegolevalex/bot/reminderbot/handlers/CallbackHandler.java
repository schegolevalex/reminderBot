package com.schegolevalex.bot.reminderbot.handlers;

import com.schegolevalex.bot.reminderbot.Constant;
import com.schegolevalex.bot.reminderbot.KeyboardFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class CallbackHandler implements Handler {
    private static CallbackHandler instance;

    private CallbackHandler() {
    }

    @Override
    public BotApiMethod<?> handle(Update update) {
        switch (update.getCallbackQuery().getData()) {
            case (Constant.GO_TO_MY_REMINDERS):
                statesDB.put(update.getCallbackQuery().getMessage().getChatId(), UserState.WATCHING_REMINDERS);
                return allReminder(update);

            case (Constant.GO_TO_ADD_REMINDER):
                statesDB.put(update.getCallbackQuery().getMessage().getChatId(), UserState.ADDING_REMINDER);
                return addReminder(update);

            default:
                SendMessage sendMessage = new SendMessage(String.valueOf(update.getMessage().getChatId()), Constant.START_DESCRIPTION);
                sendMessage.setReplyMarkup(KeyboardFactory.withStartMessage());
                statesDB.put(update.getCallbackQuery().getMessage().getChatId(), UserState.CHOOSING_FIRST_ACTION);
                return sendMessage;
        }
    }

    private BotApiMethod<?> allReminder(Update update) {
        System.out.println("*********Show all reminders*********");
        return null; //TODO
    }

    public static CallbackHandler getInstance() {
        if (instance == null) {
            instance = new CallbackHandler();
        }
        return instance;
    }
}
