package com.schegolevalex.bot.reminderbot.handler;

import com.schegolevalex.bot.reminderbot.ReminderBot;
import com.schegolevalex.bot.reminderbot.entity.Reminder;
import com.schegolevalex.bot.reminderbot.services.ReminderService;
import com.schegolevalex.bot.reminderbot.state.AbstractState;
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
    public TextHandler(Map<String, AbstractState> stateMap, Map<Long, Reminder> tempReminders, ReminderService reminderService) {
        super(stateMap);
        this.tempReminders = tempReminders;
        this.reminderService = reminderService;
    }

    @Override
    public void handle(Update update, ReminderBot bot) {
        String stateSimpleName = stateStack.peek().getClass().getSimpleName();

        switch (stateSimpleName) {
            case "AddReminderTextReplier" -> handleMessage(update, stateStack);
            case "AddReminderDateReplier" -> handleDate(update, stateStack);
            case "AddReminderTimeReplier" -> handleTime(update, stateStack);
            default -> stateStack.push(replierMap.get("wrongInputReplier"));
        }
//        reminderBot.deleteMessage(update);
    }

    private void handleMessage(Update update, Stack<AbstractState> stateStack) {
        Long chatId = AbilityUtils.getChatId(update);

        Reminder tempReminder = new Reminder();
        tempReminder.setChatId(chatId);
        tempReminder.setText(update.getMessage().getText());
        tempReminders.put(chatId, tempReminder);
        stateStack.push(replierMap.get("addReminderDateReplier"));
    }

    private void handleDate(Update update, Stack<AbstractState> stateStack) {
        Long chatId = AbilityUtils.getChatId(update);

        Reminder tempReminder = tempReminders.get(chatId);
        String text = update.getMessage().getText();
        LocalDate date;
        try {
            date = LocalDate.parse(text, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        } catch (DateTimeParseException e) {
            stateStack.push(replierMap.get("wrongInputDateReplier"));
            return;
        }
        tempReminder.setDate(date);
        stateStack.push(replierMap.get("addReminderTimeReplier"));
    }

    private void handleTime(Update update, Stack<AbstractState> stateStack) {
        Long chatId = AbilityUtils.getChatId(update);

        Reminder tempReminder = tempReminders.get(chatId);
        String text = update.getMessage().getText();
        LocalTime time;
        try {
            time = LocalTime.parse(text);
        } catch (DateTimeParseException e) {
            stateStack.push(replierMap.get("wrongInputTimeReplier"));
            return;
        }
        tempReminder.setTime(time);

        reminderService.saveReminder(tempReminder);
        reminderService.sendReminder(tempReminder);
        stateStack.push(replierMap.get("successfulAdditionReplier"));
    }
}
