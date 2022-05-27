package com.schegolevalex.bot.reminderbot.services;

import com.schegolevalex.bot.reminderbot.entities.Reminder;

import java.util.List;

public interface ReminderService {

    public List<Reminder> getAllReminders();

    public void saveReminder(Reminder reminder);

    public Reminder getReminder (long id);

    public void deleteReminder (long id);
}
