package com.schegolevalex.bot.reminderbot.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;

@Component
@ConfigurationProperties(prefix = "telegrambot")
@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BotConfiguration {
    String username;
    String botToken;
    String webhookPath;

}
