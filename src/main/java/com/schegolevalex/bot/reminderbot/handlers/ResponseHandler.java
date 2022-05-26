package com.schegolevalex.bot.reminderbot.handlers;

import com.schegolevalex.bot.reminderbot.Constant;
import com.schegolevalex.bot.reminderbot.KeyboardFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.EntityType;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Component
public class ResponseHandler {

    private Map<Long, UserState> statesDB;

    @Autowired
    private ResponseHandler(Map<Long, UserState> statesDB) {
        this.statesDB = statesDB;
    }

    // главный обработчик входящего апдейта
    public BotApiMethod<?> onUpdateReceiver(Update update) {
        if (update.hasCallbackQuery())
            return handleMessageWithCallback(update);

        if (update.hasMessage() && update.getMessage().isCommand())
            return handleMessageWithCommand(update);

        if (update.hasMessage() && update.getMessage().hasText())
            return addReminder(update);

        return new SendMessage(String.valueOf(update.getMessage().getChatId()), Constant.UNKNOWN_REQUEST);
    }

    //    обработчик апдейта с колбэком кнопок
    private BotApiMethod<?> handleMessageWithCallback(Update update) {
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

    //    обработчик апдейта с командой
    private BotApiMethod<?> handleMessageWithCommand(Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(update.getMessage().getChatId()));

//        switch (update.getMessage().getEntities().stream()
//                .map(entity -> entity.getText())
//                .filter(entity -> entity.startsWith("/"))
//                .map(entity -> entity.substring(1))
//                .findFirst().orElse("")) {
        switch (update.getMessage().getText()) {
            case ("/start"):
                sendMessage.setText(Constant.START_DESCRIPTION);
                sendMessage.setReplyMarkup(KeyboardFactory.withStartMessage());
                statesDB.put(update.getMessage().getChatId(), UserState.CHOOSING_FIRST_ACTION);
                break;

            default:
                sendMessage.setText(Constant.UNKNOWN_REQUEST);
                break;
        }

        return sendMessage;
    }

    private BotApiMethod<?> allReminder(Update update) {
        System.out.println("*********Show all reminders*********");
        return null; //TODO
    }

    private BotApiMethod<?> addReminder(Update update) {

        SendMessage sendMessage = new SendMessage();
        Long chatID;

        if (update.hasMessage()) {
            chatID = update.getMessage().getChatId();
        } else {
            chatID = update.getCallbackQuery().getMessage().getChatId();
        }

        sendMessage.setChatId(String.valueOf(chatID));

        switch (statesDB.get(chatID)) {
            case ADDING_REMINDER:
                sendMessage.setText(Constant.REMINDER_DESCRIPTION_TEXT);
                statesDB.put(chatID, UserState.ADDING_REMINDER_TEXT);
                break;

            case ADDING_REMINDER_TEXT:
                System.out.println("*********Add text to DB*********");
                //TODO
                sendMessage.setText(Constant.REMINDER_DESCRIPTION_DATE);
                statesDB.put(chatID, UserState.ADDING_REMINDER_DATE);
                break;

            case ADDING_REMINDER_DATE:
                System.out.println("*********Add date to DB*********");
                //TODO
                sendMessage.setText(Constant.REMINDER_DESCRIPTION_TIME);
                statesDB.put(chatID, UserState.ADDING_REMINDER_TIME);
                break;

            case ADDING_REMINDER_TIME:
                System.out.println("*********Add time to DB*********");
                //TODO
                statesDB.put(chatID, UserState.CHOOSING_FIRST_ACTION);
                return addReminder(update);

            default:
                update.getMessage().setText("/start");
                return handleMessageWithCommand(update);
        }
        return sendMessage;
    }


}
