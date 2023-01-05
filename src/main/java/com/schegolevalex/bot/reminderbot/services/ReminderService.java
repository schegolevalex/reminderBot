package com.schegolevalex.bot.reminderbot.services;

import com.schegolevalex.bot.reminderbot.entities.Reminder;

import java.util.List;

public interface ReminderService {

    List<Reminder> getAllReminders();

    void saveReminder(Reminder reminder);

    Reminder getReminder(long id);

    void deleteReminder(long id);

    List<Reminder> getAllRemindersById(Long chatId);
}
