package com.schegolevalex.bot.reminderbot.services;

import com.schegolevalex.bot.reminderbot.entity.Reminder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReminderService {

    List<Reminder> getAllReminders();

    Reminder saveReminder(Reminder reminder);

    Optional<Reminder> getReminderById(UUID id);

    void deleteReminder(UUID id);

    List<Reminder> getAllRemindersByChatId(Long chatId);

}
