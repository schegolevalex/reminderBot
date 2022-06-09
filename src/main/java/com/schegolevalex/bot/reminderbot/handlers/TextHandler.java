package com.schegolevalex.bot.reminderbot.handlers;

import com.schegolevalex.bot.reminderbot.Constant;
import com.schegolevalex.bot.reminderbot.ReminderBot;
import com.schegolevalex.bot.reminderbot.entities.Reminder;
import com.schegolevalex.bot.reminderbot.services.ReminderService;
import com.schegolevalex.bot.reminderbot.states.*;
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
    protected TextHandler(AwaitingStartState awaitingStartState,
                          ChoosingFirstActionState choosingFirstActionState,
                          WatchingRemindersState watchingRemindersState,
                          AddingReminderTextState addingReminderTextState,
                          AddingReminderDateState addingReminderDateState,
                          AddingReminderTimeState addingReminderTimeState,
                          WrongInputState wrongInputState,
                          Map<Long, Reminder> reminders,
                          ReminderService reminderService) {
        super(awaitingStartState, choosingFirstActionState, watchingRemindersState,
                addingReminderTextState, addingReminderDateState, addingReminderTimeState, wrongInputState);
        this.reminders = reminders;
        this.reminderService = reminderService;
    }

        @Override
    public void handle(Update update, Stack<UserState> userState) {
        Long chatId = AbilityUtils.getChatId(update);

        switch (userState.peek()) {
            case addingReminderTextState: {
                this.reminders.put(chatId, new Reminder());
                Reminder newReminder = this.reminders.get(chatId);
                newReminder.setChatID(chatId);
                newReminder.setText(update.getMessage().getText());
                userState.push(addingReminderDateState);
                break;
            }
            case addingReminderDateState: {
                Reminder newReminder = this.reminders.get(chatId);
                String text = update.getMessage().getText();
                LocalDate date = null;

                try {
                    date = LocalDate.parse(text, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
                } catch (Exception e) {
                    sendMessage.setText(Constant.WRONG_DATE_FORMAT);
                }

                if (date != null) {
                    newReminder.setDate(date);
                    sendMessage.setText(Constant.ADDING_REMINDER_TIME_DESCRIPTION);
                    userState.push(addingReminderTimeState);
                } else {
                    sendMessage.setText(Constant.WRONG_DATE_FORMAT);
                }
                break;
            }
            case addingReminderTimeState: {
                Reminder newReminder = this.reminders.get(chatId);
                String text = update.getMessage().getText();
                LocalTime time = null;

                try {
                    time = LocalTime.parse(text);
                } catch (Exception e) {
                    sendMessage.setText(Constant.WRONG_TIME_FORMAT);
                }

                if (time != null) {
                    newReminder.setTime(time);
                    this.reminderService.saveReminder(newReminder);
                    reminderBot.sendReminder(newReminder);
                    userState.push(choosingFirstActionState);
                    return handle(update, userState);
                } else {
                    sendMessage.setText(Constant.WRONG_TIME_FORMAT);
                }
            }
            default: {
                // TODO
                break;
            }
        }
    }
}
