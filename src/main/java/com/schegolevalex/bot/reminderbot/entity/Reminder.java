package com.schegolevalex.bot.reminderbot.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "reminders")
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Reminder implements Comparable<Reminder> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reminder_id", nullable = false)
    long reminderId;

    @Column(name = "chat_id", nullable = false)
    long chatId;

    @Column(name = "reminder_text", nullable = false)
    String text;

    @Column(name = "reminder_date", nullable = false)
    LocalDate date;

    @Column(name = "reminder_time", nullable = false)
    LocalTime time;

    public Reminder(long chatId, String text, LocalDate date, LocalTime time) {
        this.chatId = chatId;
        this.text = text;
        this.date = date;
        this.time = time;
    }

    @Override
    public String toString() {
        return "Reminder{" +
                "reminderId=" + reminderId +
                ", chatId=" + chatId +
                ", text='" + text + '\'' +
                ", date=" + date +
                ", time=" + time +
                '}';
    }

    @Override
    public int compareTo(Reminder other) {
        LocalDateTime thisReminderDateTime = this.getDate().atTime(this.getTime());
        LocalDateTime otherReminderDateTime = other.getDate().atTime(other.getTime());
        return thisReminderDateTime.compareTo(otherReminderDateTime);
    }
}