package com.schegolevalex.bot.reminderbot.services;

import com.schegolevalex.bot.reminderbot.Constant;
import com.schegolevalex.bot.reminderbot.entities.Reminder;
import com.schegolevalex.bot.reminderbot.repositories.ReminderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

@Service
public class ReminderServiceImpl implements ReminderService {

    private final TelegramWebhookBot bot;
    private final ReminderRepository reminderRepository;
    private final ThreadPoolTaskScheduler taskScheduler;


    @Autowired
    public ReminderServiceImpl(TelegramWebhookBot bot,
                               ReminderRepository reminderRepository,
                               ThreadPoolTaskScheduler taskScheduler) {
        this.bot = bot;
        this.reminderRepository = reminderRepository;
        this.taskScheduler = taskScheduler;
    }

    @Override
    public List<Reminder> getAllReminders() {
        return reminderRepository.findAll();
    }

    @Override
    public void saveReminder(Reminder reminder) {
        reminderRepository.save(reminder);
    }

    @Override
    public Reminder getReminder(long id) {
        return reminderRepository.findById(id).get();
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

    private void executeMethod(BotApiMethod<?> method) {
        int messageId;
        try {
            if (method instanceof SendMessage) {
                messageId = bot.execute((SendMessage) method).getMessageId();
//                reminderFacade.putToMessageIds(((SendMessage) method).getChatId(), messageId);
            } else {
                bot.execute(method);
            }
        } catch (TelegramApiException e) {
            e.printStackTrace(); //todo залогировать ошибку и отправить пользователю сообщение, что произошла ошибка
        }
    }

    @PostConstruct
    private void registerPreviousReminders() {
        List<Reminder> allReminders = getAllReminders();
        for (Reminder reminder : allReminders)
            sendReminder(reminder);
    }
}
