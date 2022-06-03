package com.schegolevalex.bot.reminderbot.handlers;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;
import com.schegolevalex.bot.reminderbot.Constant;
import com.schegolevalex.bot.reminderbot.KeyboardFactory;
import com.schegolevalex.bot.reminderbot.entities.Reminder;
import com.schegolevalex.bot.reminderbot.services.ReminderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.util.AbilityUtils;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class TextHandler implements Handler {
    private Map<Long, UserState> userStates;
    private Map<Long, Reminder> reminders;
    private ReminderService reminderService;

    @Autowired
    private TextHandler(Map<Long, UserState> userStates, Map<Long, Reminder> reminders, ReminderService reminderService) {
        this.userStates = userStates;
        this.reminders = reminders;
        this.reminderService = reminderService;
    }

    @Override
    public BotApiMethod<?> handle(Update update) {
        Long chatId = AbilityUtils.getChatId(update);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));

        switch (userStates.get(chatId)) {
            case ADDING_REMINDER_TEXT: {
                reminders.put(chatId, new Reminder());
                Reminder newReminder = reminders.get(chatId);
                newReminder.setChatID(chatId);
                newReminder.setText(update.getMessage().getText());
                sendMessage.setText(Constant.REMINDER_DESCRIPTION_DATE);
                userStates.put(chatId, UserState.ADDING_REMINDER_DATE);
                break;
            }
            case ADDING_REMINDER_DATE: {
                Reminder newReminder = reminders.get(chatId);
                String text = update.getMessage().getText();
                LocalDate date = null;

                try {
                    date = LocalDate.parse(text, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
                } catch (Exception e) {
                    sendMessage.setText(Constant.WRONG_DATE_FORMAT);
                }

                if (date != null) {
                    newReminder.setDate(date);
                    sendMessage.setText(Constant.REMINDER_DESCRIPTION_TIME);
                    userStates.put(chatId, UserState.ADDING_REMINDER_TIME);
                } else {
                    sendMessage.setText(Constant.WRONG_DATE_FORMAT);
                }
                break;
            }
            case ADDING_REMINDER_TIME: {
                Reminder newReminder = reminders.get(chatId);
                String text = update.getMessage().getText();
                LocalTime time = null;

                try {
                    time = LocalTime.parse(text);
                } catch (Exception e) {
                    sendMessage.setText(Constant.WRONG_TIME_FORMAT);
                }

                if (time != null) {
                    newReminder.setTime(time);
                    reminderService.saveReminder(newReminder);
                    userStates.put(chatId, UserState.CHOOSING_FIRST_ACTION);
                    return handle(update);
                } else {
                    sendMessage.setText(Constant.WRONG_TIME_FORMAT);
                }
            }
            default: {
                sendMessage.setText(Constant.START_DESCRIPTION);
                sendMessage.setReplyMarkup(KeyboardFactory.withStartMessage());
                break;
            }
        }
        return sendMessage;
    }
}
