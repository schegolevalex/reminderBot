package com.schegolevalex.bot.reminderbot.handlers;

import com.schegolevalex.bot.reminderbot.ReminderBot;
import com.schegolevalex.bot.reminderbot.entities.Reminder;
import com.schegolevalex.bot.reminderbot.services.ReminderService;
import com.schegolevalex.bot.reminderbot.states.UserState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.util.AbilityUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
                          @Lazy ReminderBot reminderBot) {
        this.tempReminders = tempReminders;
        this.reminderService = reminderService;
        this.reminderBot = reminderBot;
    }

    @Override
    public void handle(Update update, Stack<UserState> userStateStack) {
        String stateSimpleName = userStateStack.peek().getClass().getSimpleName();

        switch (stateSimpleName) {
            case "AddReminderTextState" -> handleText(update, userStateStack);
            case "AddReminderDateState" -> handleDate(update, userStateStack);
            case "AddReminderTimeState" -> handleTime(update, userStateStack);
            default -> userStateStack.push(statesMap.get("wrongInputState"));
        }
        reminderBot.deleteMessage(update);
    }

    private void handleText(Update update, Stack<UserState> userStateStack) {
        Long chatId = AbilityUtils.getChatId(update);

        Reminder tempReminder = new Reminder();
        tempReminder.setChatID(chatId);
        tempReminder.setText(update.getMessage().getText());
        tempReminders.put(chatId, tempReminder);
        userStateStack.push(statesMap.get("addReminderDateState"));
    }

    private void handleDate(Update update, Stack<UserState> userStateStack) {
        Long chatId = AbilityUtils.getChatId(update);

        Reminder tempReminder = tempReminders.get(chatId);
        String text = update.getMessage().getText();
        LocalDate date;
        try {
            date = LocalDate.parse(text, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        } catch (DateTimeParseException e) {
            userStateStack.push(statesMap.get("wrongInputState"));
            return;
        }
        tempReminder.setDate(date);
        userStateStack.push(statesMap.get("addReminderTimeState"));
    }

    private void handleTime(Update update, Stack<UserState> userStateStack) {
        Long chatId = AbilityUtils.getChatId(update);

        Reminder tempReminder = tempReminders.get(chatId);
        String text = update.getMessage().getText();
        LocalTime time;
        try {
            time = LocalTime.parse(text);
        } catch (DateTimeParseException e) {
            userStateStack.push(statesMap.get("wrongInputState"));
            return;
        }
        tempReminder.setTime(time);

        reminderService.saveReminder(tempReminder);
        reminderBot.sendReminder(tempReminder);
        userStateStack.push(statesMap.get("successfulAdditionState"));
    }
}
