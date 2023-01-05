package com.schegolevalex.bot.reminderbot.states;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;

import java.util.Map;

@Component
public abstract class UserState {
    final TelegramWebhookBot bot;

    @Autowired
    public UserState(TelegramWebhookBot bot) {
        this.bot = bot;
    }

    public abstract void sendReply(Long chatId, Map<String, Integer> messageIds);
}