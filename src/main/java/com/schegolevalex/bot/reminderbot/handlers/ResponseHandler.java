package com.schegolevalex.bot.reminderbot.handlers;

import com.schegolevalex.bot.reminderbot.Constant;
import com.schegolevalex.bot.reminderbot.KeyboardFactory;
import com.schegolevalex.bot.reminderbot.entities.Reminder;
import com.schegolevalex.bot.reminderbot.services.ReminderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.sql.Timestamp;
import java.util.Map;

@Component
@Scope("prototype")
public class ResponseHandler {

    private Map<Long, UserState> statesDB;
    private ReminderServiceImpl reminderService;

    private Reminder reminder;

    private String date;

    @Autowired
    private ResponseHandler(Map<Long, UserState> statesDB, ReminderServiceImpl reminderService) {
        this.statesDB = statesDB; // возможно в будущем можно будет отказаться от внедрения этого объекта спрингом, при условии, что мапа состояний не понадобиться нигде дальше
        this.reminderService = reminderService;
    }

    public BotApiMethod<?> onUpdateReceiver(Update update) {
        if (update.hasCallbackQuery())
            return handleMessageWithCallback(update);

        if (update.hasMessage() && update.getMessage().isCommand())
            return handleMessageWithCommand(update);

        if (update.hasMessage() && update.getMessage().hasText())
            return addReminder(update);

        return new SendMessage(String.valueOf(update.getMessage().getChatId()), Constant.UNKNOWN_REQUEST);
    }

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


}
