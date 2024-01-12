package com.schegolevalex.bot.reminderbot.repliers;

import com.schegolevalex.bot.reminderbot.Constant;
import com.schegolevalex.bot.reminderbot.KeyboardFactory;
import com.schegolevalex.bot.reminderbot.entities.Reminder;
import com.schegolevalex.bot.reminderbot.services.ReminderService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DeleteReminderReplier extends AbstractReplier {
    private final ReminderService reminderService;

    public DeleteReminderReplier(TelegramWebhookBot bot, ReminderService reminderService) {
        super(bot);
        this.reminderService = reminderService;
    }

    @Override
    public void reply(Long chatId, Map<String, Integer> messageIds) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(String.valueOf(chatId));

        List<Reminder> reminders = reminderService.getAllRemindersById(chatId);
        reminders = reminders.stream().sorted().collect(Collectors.toList());

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
    }
}
