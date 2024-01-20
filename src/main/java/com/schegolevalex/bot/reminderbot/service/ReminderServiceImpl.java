package com.schegolevalex.bot.reminderbot.service;

import com.schegolevalex.bot.reminderbot.entity.Reminder;
import com.schegolevalex.bot.reminderbot.repository.ReminderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReminderServiceImpl implements ReminderService {

    private final ReminderRepository reminderRepository;

    @Override
    public List<Reminder> getAllReminders() {
        return reminderRepository.findAll();
    }

    @Override
    public Reminder saveReminder(Reminder reminder) {
        return reminderRepository.save(reminder);
    }

    @Override
    public Optional<Reminder> getReminderById(UUID id) {
        return reminderRepository.findById(id);
    }

    @Override
    public void deleteReminder(UUID id) {
        reminderRepository.deleteById(id);
    }

    @Override
    public List<Reminder> getAllRemindersByChatId(Long chatId) {
        return reminderRepository.findAllByChatId(chatId);
    }
}
