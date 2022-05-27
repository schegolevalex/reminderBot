package com.schegolevalex.bot.reminderbot.entities;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.sql.Timestamp;

@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "reminders")
public class Reminder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reminder_id", nullable = false)
    long reminderID;

    @Column(name = "chat_id", nullable = false)
    long chatID;

    @Column(name = "reminder_text", nullable = false)
    String text;

    @Column(name = "reminder_timestamp", nullable = false)
    Timestamp date;

    public Reminder(long chatID, String text, Timestamp date) {
        this.chatID = chatID;
        this.text = text;
        this.date = date;
    }

    @Override
    public String toString() {
        return "Reminder{" +
                "reminderID=" + reminderID +
                ", chatID=" + chatID +
                ", text='" + text + '\'' +
                ", date=" + date +
                '}';
    }
}