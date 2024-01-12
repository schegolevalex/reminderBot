package com.schegolevalex.bot.reminderbot.services;

import com.schegolevalex.bot.reminderbot.Constant;
import com.schegolevalex.bot.reminderbot.entities.Reminder;
import com.schegolevalex.bot.reminderbot.repositories.ReminderRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReminderServiceImpl implements ReminderService {

    private final TelegramWebhookBot bot;
    private final ReminderRepository reminderRepository;
    private final ThreadPoolTaskScheduler taskScheduler;

    @Override
    public List<Reminder> getAllReminders() {
        return reminderRepository.findAll();
    }

    @Override
    public void saveReminder(Reminder reminder) {
        reminderRepository.save(reminder);
    }

    @Override
    public Optional<Reminder> getReminder(long id) {
        return reminderRepository.findById(id);
    }

    @Override
    public void deleteReminder(long id) {
        reminderRepository.deleteById(id);
    }

    @Override
    public List<Reminder> getAllRemindersById(Long chatId) {
        return reminderRepository.findAllByChatID(chatId);
    }

    @Override
    public void sendReminder(Reminder reminder) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reminderDateTime = reminder.getDate().atTime(reminder.getTime());

        if (now.isBefore(reminderDateTime)) {
            Date date = Date.from(reminderDateTime.toInstant(ZoneOffset.of("+3")));

            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(reminder.getChatID()));
            message.setText(String.format(Constant.REMINDER_MESSAGE, reminder.getTime(), reminder.getText()));

            taskScheduler.schedule(() -> executeMethod(message), date);
        }
    }

    @SneakyThrows
    private void executeMethod(BotApiMethod<?> method) {
        int messageId;
        if (method instanceof SendMessage) {
            messageId = bot.execute((SendMessage) method).getMessageId();
//                reminderFacade.putToMessageIds(((SendMessage) method).getChatId(), messageId);
        } else {
            bot.execute(method);
        }
    }

    @PostConstruct
    private void registerPreviousReminders() {
        List<Reminder> allReminders = getAllReminders();
        for (Reminder reminder : allReminders)
            sendReminder(reminder);
    }
}
