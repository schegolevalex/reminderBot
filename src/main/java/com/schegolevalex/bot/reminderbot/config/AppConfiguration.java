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
    BotConfiguration botConfiguration;

    @Bean
    public ReminderBot reminderBot() throws TelegramApiException {
        return new ReminderBot();
    }

    @Bean
    public TelegramBotsApi telegramBotsApi() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(reminderBot());
        return telegramBotsApi;
    }


}
