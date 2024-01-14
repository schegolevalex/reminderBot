package com.schegolevalex.bot.reminderbot.state;

import com.schegolevalex.bot.reminderbot.Constant;
import com.schegolevalex.bot.reminderbot.KeyboardFactory;
import com.schegolevalex.bot.reminderbot.ReminderBot;
import com.schegolevalex.bot.reminderbot.entity.Reminder;
import com.schegolevalex.bot.reminderbot.handler.Handler;
import com.schegolevalex.bot.reminderbot.services.ReminderService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Component
public class WatchRemindersState extends AbstractState {
    private final ReminderService reminderService;

    @Autowired
    public WatchRemindersState(Map<String, Handler> handlerMap,
                               ReminderBot bot,
                               ReminderService reminderService) {
        super(handlerMap, bot);
        this.reminderService = reminderService;
    }


    @SneakyThrows
    @Override
    public BotApiMethod<?> reply(Long chatId, Update update) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(String.valueOf(chatId));

        List<Reminder> reminders = reminderService.getAllRemindersById(chatId);
        reminders = reminders.stream().sorted().toList();

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
        editMessageText.setText(String.valueOf(text));
        editMessageText.setReplyMarkup(KeyboardFactory.withBackButton());
        editMessageText.setMessageId(messageIds.get(String.valueOf(chatId)));
        return editMessageText;
    }
}
