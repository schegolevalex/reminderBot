package com.schegolevalex.bot.reminderbot.handlers;

import com.schegolevalex.bot.reminderbot.ReminderBot;
import com.schegolevalex.bot.reminderbot.entities.Reminder;
import com.schegolevalex.bot.reminderbot.services.ReminderService;
import com.schegolevalex.bot.reminderbot.states.UserState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.util.AbilityUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Stack;

@Component
public class TextHandler extends Handler {
    private final Map<Long, Reminder> tempReminders;
    private final ReminderService reminderService;
    private final ReminderBot reminderBot;

    @Autowired
    protected TextHandler(Map<Long, Reminder> tempReminders,
                          ReminderService reminderService,
                          ReminderBot reminderBot) {
        this.tempReminders = tempReminders;
        this.reminderService = reminderService;
        this.reminderBot = reminderBot;
    }

    @Override
    public void handle(Update update, Stack<UserState> userState) {
        String name = userState.peek().getClass().getSimpleName();

        switch (name) {
            case "AddingReminderTextState":
                handleText(update, userState);
                break;
            case "AddingReminderDateState":
                handleDate(update, userState);
                break;
            case "AddingReminderTimeState":
                handleTime(update, userState);
                break;
            default:
                userState.push(statesMap.get("wrongInputState"));
                break;
        }
        reminderBot.deleteMessage(update);
    }

    private void handleText(Update update, Stack<UserState> userState) {
        Long chatId = AbilityUtils.getChatId(update);

        Reminder tempReminder = new Reminder();
        tempReminder.setChatID(chatId);
        tempReminder.setText(update.getMessage().getText());
        tempReminders.put(chatId, tempReminder);
        userState.push(statesMap.get("addingReminderDateState"));
    }

    private void handleDate(Update update, Stack<UserState> userState) {
        Long chatId = AbilityUtils.getChatId(update);

        Reminder tempReminder = tempReminders.get(chatId);
        String text = update.getMessage().getText();
        LocalDate date;
        try {
            date = LocalDate.parse(text, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        } catch (Exception e) {
            userState.push(statesMap.get("wrongInputState"));
            return;
        }
        tempReminder.setDate(date);
        userState.push(statesMap.get("addingReminderTimeState"));
    }

    private void handleTime(Update update, Stack<UserState> userState) {
        Long chatId = AbilityUtils.getChatId(update);

        Reminder tempReminder = tempReminders.get(chatId);
        String text = update.getMessage().getText();
        LocalTime time;
        try {
            time = LocalTime.parse(text);
        } catch (Exception e) {
            userState.push(statesMap.get("wrongInputState"));
            return;
        }
        tempReminder.setTime(time);

        reminderService.saveReminder(tempReminder);
        reminderBot.sendReminder(tempReminder);
        userState.push(statesMap.get("successfulAdditionState"));
    }
}
