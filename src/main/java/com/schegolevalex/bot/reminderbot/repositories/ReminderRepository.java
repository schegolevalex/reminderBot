package com.schegolevalex.bot.reminderbot.repositories;

import com.schegolevalex.bot.reminderbot.entities.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Long> {
    public List<Reminder> findRemindersByChatID(Long chatId);
}
