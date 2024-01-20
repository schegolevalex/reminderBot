package com.schegolevalex.bot.reminderbot.config;

import com.schegolevalex.bot.reminderbot.entity.Reminder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class AppConfiguration {

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
