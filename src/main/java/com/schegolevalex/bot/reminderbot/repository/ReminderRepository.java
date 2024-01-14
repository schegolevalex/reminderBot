package com.schegolevalex.bot.reminderbot.repository;

import com.schegolevalex.bot.reminderbot.entity.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Long> {
    List<Reminder> findAllByChatId(Long chatId);
}
