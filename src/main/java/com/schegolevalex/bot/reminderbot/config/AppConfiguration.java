package com.schegolevalex.bot.reminderbot.config;

import com.schegolevalex.bot.reminderbot.handlers.UserState;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;


@Configuration
@ComponentScan(basePackages = "com.schegolevalex.bot.reminderbot")
public class AppConfiguration {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public Map<Long, UserState> statesDB() {
        return new HashMap<>();
    }
}
