package com.schegolevalex.bot.reminderbot.configs;

import com.schegolevalex.bot.reminderbot.entities.Reminder;
import com.schegolevalex.bot.reminderbot.states.UserState;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;


@Configuration
@ComponentScan(basePackages = "com.schegolevalex.bot.reminderbot")
public class AppConfiguration {

    @Bean
    public Map<Long, Stack<UserState>> userStates() {
        return new HashMap<>();
    }

    @Bean
    public Map<Long, Reminder> tempReminders() {
        return new HashMap<>();
    }

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler threadPool = new ThreadPoolTaskScheduler();
        threadPool.setPoolSize(5);
//        threadPool.initialize();
        return threadPool;
    }
}
