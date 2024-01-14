package com.schegolevalex.bot.reminderbot.services;

import com.schegolevalex.bot.reminderbot.entity.Reminder;

import java.util.List;
import java.util.Optional;

public interface ReminderService {

    List<Reminder> getAllReminders();

    void saveReminder(Reminder reminder);

    Optional<Reminder> getReminder(long id);

    void deleteReminder(long id);

    List<Reminder> getAllRemindersById(Long chatId);

    void sendReminder(Reminder reminder);
}
