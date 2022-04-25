package com.schegolevalex.bot.reminderbot.config;

import com.schegolevalex.bot.reminderbot.ReminderBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class AppConfiguration {
    @Autowired
    private BotConfiguration botConfiguration;

    @Autowired
    private ReminderBot bot;

    @Bean
    public TelegramBotsApi telegramBotsApi() {
        TelegramBotsApi telegramBotsApi = null;
        try {
            telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
        return telegramBotsApi;
    }


}
