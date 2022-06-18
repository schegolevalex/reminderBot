package com.schegolevalex.bot.reminderbot.states;

import com.schegolevalex.bot.reminderbot.Constant;
import com.schegolevalex.bot.reminderbot.KeyboardFactory;
import com.schegolevalex.bot.reminderbot.entities.Reminder;
import com.schegolevalex.bot.reminderbot.handlers.HandlerFactory;
import com.schegolevalex.bot.reminderbot.services.ReminderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class WatchingRemindersState extends UserState {
    private final ReminderServiceImpl reminderService;

    @Autowired
    public WatchingRemindersState(@Lazy HandlerFactory handlerFactory,
                                  ReminderServiceImpl reminderService) {
        super(handlerFactory);
        this.reminderService = reminderService;
    }

    @Override
    public SendMessage setText(SendMessage sendMessage) {
        List<Reminder> reminders = reminderService.getAllRemindersById(Long.valueOf(sendMessage.getChatId()));
        StringBuilder text = new StringBuilder(Constant.MY_REMINDERS);

        if (reminders.isEmpty()) {
            text.append(Constant.REMINDER_LIST_IS_EMPTY);
        } else {
            LocalDateTime now = LocalDateTime.now();
            for (Reminder reminder : reminders) {
                LocalDateTime reminderDateTime = reminder.getDate().atTime(reminder.getTime());
                if (now.isBefore(reminderDateTime)) {
                    text.append("⏳ ");
                } else {
                    text.append("✅ ");
                }

                text.append(reminder.getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                        .append(" ")
                        .append(reminder.getTime())
                        .append(" ")
                        .append(reminder.getText())
                        .append("\n");
            }
        }
        sendMessage.setText(String.valueOf(text));
        sendMessage.setReplyMarkup(KeyboardFactory.withBackButton());
        return sendMessage;
    }
}
