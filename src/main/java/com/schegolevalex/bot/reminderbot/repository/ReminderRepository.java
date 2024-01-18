package com.schegolevalex.bot.reminderbot.repository;

import com.schegolevalex.bot.reminderbot.entity.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, UUID> {
    List<Reminder> findAllByChatId(Long chatId);
}
