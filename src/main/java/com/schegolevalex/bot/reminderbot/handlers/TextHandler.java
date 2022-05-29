package com.schegolevalex.bot.reminderbot.handlers;

import com.schegolevalex.bot.reminderbot.Constant;
import com.schegolevalex.bot.reminderbot.entities.Reminder;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.sql.Timestamp;

@Component
public class TextHandler implements Handler {
    private static TextHandler instance;

    private TextHandler() {
    }

    @Override
    public BotApiMethod<?> handle(Update update) {
        SendMessage sendMessage = new SendMessage();
        Long chatID;

        if (update.hasMessage()) {
            chatID = update.getMessage().getChatId();
        } else {
            chatID = update.getCallbackQuery().getMessage().getChatId();
        }

        sendMessage.setChatId(String.valueOf(chatID));

        if (statesDB.get(chatID) == UserState.ADDING_REMINDER_DATE) {
            date = update.getMessage().getText();
        }

        switch (statesDB.get(chatID)) {
            case ADDING_REMINDER:
                reminder = new Reminder();
                reminder.setChatID(chatID);
                sendMessage.setText(Constant.REMINDER_DESCRIPTION_TEXT);
                statesDB.put(chatID, UserState.ADDING_REMINDER_TEXT);
                break;

            case ADDING_REMINDER_TEXT:
                reminder.setText(update.getMessage().getText());
                sendMessage.setText(Constant.REMINDER_DESCRIPTION_DATE);
                statesDB.put(chatID, UserState.ADDING_REMINDER_DATE);
                break;

            case ADDING_REMINDER_DATE:
                sendMessage.setText(Constant.REMINDER_DESCRIPTION_TIME);
                statesDB.put(chatID, UserState.ADDING_REMINDER_TIME);
                break;

            case ADDING_REMINDER_TIME:
                reminder.setDate(Timestamp.valueOf(date + " " + update.getMessage().getText()));
                reminderService.saveReminder(reminder);
                statesDB.put(chatID, UserState.CHOOSING_FIRST_ACTION);
                return addReminder(update);

            default:
                update.getMessage().setText("/start");
                return handleMessageWithCommand(update);
        }
        return sendMessage;
    }

    public static TextHandler getInstance() {
        if (instance == null) {
            instance = new TextHandler();
        }
        return instance;
    }
}
