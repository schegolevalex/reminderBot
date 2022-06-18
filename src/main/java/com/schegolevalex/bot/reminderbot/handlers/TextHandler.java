package com.schegolevalex.bot.reminderbot.handlers;

import com.schegolevalex.bot.reminderbot.Constant;
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
    private final Map<Long, Reminder> reminders;
    private final ReminderService reminderService;
    private final ReminderBot reminderBot;

    @Autowired
    protected TextHandler(Map<Long, Reminder> reminders,
                          ReminderService reminderService,
                          ReminderBot reminderBot) {
        this.reminders = reminders;
        this.reminderService = reminderService;
        this.reminderBot = reminderBot;
    }

    @Override
    public void handle(Update update, Stack<UserState> userState) {
        Long chatId = AbilityUtils.getChatId(update);

        UserState peek = userState.peek();

        if (states.get("addingReminderTextState").equals(peek)) {
            this.reminders.put(chatId, new Reminder());
            Reminder tempReminder = this.reminders.get(chatId);
            tempReminder.setChatID(chatId);
            tempReminder.setText(update.getMessage().getText());
            userState.push(states.get("addingReminderDateState"));

        } else if (states.get("addingReminderDateState").equals(peek)) {
            Reminder newReminder = this.reminders.get(chatId);
            String text = update.getMessage().getText();
            LocalDate date = null;

            try {
                date = LocalDate.parse(text, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            } catch (Exception e) {
                userState.push(states.get("wrongInputState"));
                return;
            }
            newReminder.setDate(date);
            userState.push(states.get("addingReminderTimeState"));

        } else if (states.get("addingReminderTimeState").equals(peek)) {
            Reminder newReminder = this.reminders.get(chatId);
            String text = update.getMessage().getText();
            LocalTime time = null;

            try {
                time = LocalTime.parse(text);
            } catch (Exception e) {
                userState.push(states.get("wrongInputState"));
                return;
            }
            newReminder.setTime(time);

            this.reminderService.saveReminder(newReminder);
            reminderBot.sendReminder(newReminder);
            reminderBot.sendMessage(Constant.SUCCESSFUL_ADDITION, chatId);
            userState.push(states.get("choosingFirstActionState"));

        } else {
            userState.push(states.get("wrongInputState"));
        }
    }
}
