package com.schegolevalex.bot.reminderbot.entities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
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
    long reminderID;

    @Column(name = "chat_id", nullable = false)
    long chatID;

    @Column(name = "reminder_text", nullable = false)
    String text;

    @Column(name = "reminder_date", nullable = false)
    LocalDate date;

    @Column(name = "reminder_time", nullable = false)
    LocalTime time;

    public Reminder(long chatID, String text, LocalDate date, LocalTime time) {
        this.chatID = chatID;
        this.text = text;
        this.date = date;
        this.time = time;
    }

    @Override
    public String toString() {
        return "Reminder{" +
                "reminderID=" + reminderID +
                ", chatID=" + chatID +
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