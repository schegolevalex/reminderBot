package com.schegolevalex.bot.reminderbot.handlers;

import com.schegolevalex.bot.reminderbot.Constant;
import com.schegolevalex.bot.reminderbot.KeyboardFactory;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.util.AbilityUtils;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class CallbackHandler implements Handler {
    private static CallbackHandler instance;

    private CallbackHandler() {
    }

    @Override
    public BotApiMethod<?> handle(Update update, UserState state) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(AbilityUtils.getChatId(update)));

        switch (update.getCallbackQuery().getData()) {
            case (Constant.GO_TO_MY_REMINDERS):
                state = UserState.WATCHING_REMINDERS; //заглушка, осталось от старого, переписать TODO
                return allReminder(update);

            case (Constant.GO_TO_ADD_REMINDER):
                sendMessage.setText(Constant.REMINDER_DESCRIPTION_TEXT);
                state = UserState.ADDING_REMINDER_TEXT;
                break;

            default:
                sendMessage.setText(Constant.START_DESCRIPTION);
                sendMessage.setReplyMarkup(KeyboardFactory.withStartMessage());
                state = UserState.CHOOSING_FIRST_ACTION;
                break;
        }
        return sendMessage;
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
