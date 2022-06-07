package com.schegolevalex.bot.reminderbot.services;

import com.schegolevalex.bot.reminderbot.entities.Reminder;
import com.schegolevalex.bot.reminderbot.repositories.ReminderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReminderServiceImpl implements ReminderService {

    private final ReminderRepository reminderRepository;

    @Autowired
    public ReminderServiceImpl(ReminderRepository reminderRepository) {
        this.reminderRepository = reminderRepository;
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
        Reminder reminder = null;
        Optional<Reminder> optional = reminderRepository.findById(id);

        if (optional.isPresent())
            reminder = optional.get();

        return reminder;
    }

    @Override
    public void deleteReminder(long id) {
        reminderRepository.deleteById(id);
    }

    public List<Reminder> getAllRemindersById(Long chatId) {
        return reminderRepository.findRemindersByChatID(chatId);
    }
}
