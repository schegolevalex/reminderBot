package com.schegolevalex.bot.reminderbot.config;

import com.schegolevalex.bot.reminderbot.entities.Reminder;
import com.schegolevalex.bot.reminderbot.handlers.UserState;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.HashMap;
import java.util.Map;


@Configuration
@ComponentScan(basePackages = "com.schegolevalex.bot.reminderbot")
public class AppConfiguration {

//    @Bean
//    public RestTemplate restTemplate() {
//        return new RestTemplate();
//    }

    @Bean
    public Map<Long, UserState> userStates() {
        return new HashMap<>();
    }

    @Bean
    public Map<Long, Reminder> reminders() {
        return new HashMap<>();
    }
}
