package com.schegolevalex.bot.reminderbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ReminderBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReminderBotApplication.class, args);
    }
}
