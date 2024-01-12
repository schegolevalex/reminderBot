package com.schegolevalex.bot.reminderbot.handlers;

import com.schegolevalex.bot.reminderbot.entities.Reminder;
import com.schegolevalex.bot.reminderbot.repliers.AbstractReplier;
import com.schegolevalex.bot.reminderbot.services.ReminderService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    protected TextHandler(Map<Long, Reminder> tempReminders,
                          ReminderService reminderService) {
        this.tempReminders = tempReminders;
        this.reminderService = reminderService;
    }

    @Override
    public void handle(Update update, Stack<AbstractReplier> replierStack) {
        String stateSimpleName = replierStack.peek().getClass().getSimpleName();

        switch (stateSimpleName) {
            case "AddReminderTextReplier" -> handleMessage(update, replierStack);
            case "AddReminderDateReplier" -> handleDate(update, replierStack);
            case "AddReminderTimeReplier" -> handleTime(update, replierStack);
            default -> replierStack.push(replierMap.get("wrongInputReplier"));
        }
//        reminderBot.deleteMessage(update);
    }

    private void handleMessage(Update update, Stack<AbstractReplier> replierStack) {
        Long chatId = AbilityUtils.getChatId(update);

        Reminder tempReminder = new Reminder();
        tempReminder.setChatID(chatId);
        tempReminder.setText(update.getMessage().getText());
        tempReminders.put(chatId, tempReminder);
        replierStack.push(replierMap.get("addReminderDateReplier"));
    }

    private void handleDate(Update update, Stack<AbstractReplier> replierStack) {
        Long chatId = AbilityUtils.getChatId(update);

        Reminder tempReminder = tempReminders.get(chatId);
        String text = update.getMessage().getText();
        LocalDate date;
        try {
            date = LocalDate.parse(text, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        } catch (DateTimeParseException e) {
            replierStack.push(replierMap.get("wrongInputDateReplier"));
            return;
        }
        tempReminder.setDate(date);
        replierStack.push(replierMap.get("addReminderTimeReplier"));
    }

    private void handleTime(Update update, Stack<AbstractReplier> replierStack) {
        Long chatId = AbilityUtils.getChatId(update);

        Reminder tempReminder = tempReminders.get(chatId);
        String text = update.getMessage().getText();
        LocalTime time;
        try {
            time = LocalTime.parse(text);
        } catch (DateTimeParseException e) {
            replierStack.push(replierMap.get("wrongInputTimeReplier"));
            return;
        }
        tempReminder.setTime(time);

        reminderService.saveReminder(tempReminder);
        reminderService.sendReminder(tempReminder);
        replierStack.push(replierMap.get("successfulAdditionReplier"));
    }
}
