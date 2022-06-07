package com.schegolevalex.bot.reminderbot.configs;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

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
