package com.schegolevalex.bot.reminderbot.repliers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;

import java.util.Map;

@Component
public abstract class AbstractReplier {
    protected final TelegramWebhookBot bot;

    @Autowired
    public AbstractReplier(TelegramWebhookBot bot) {
        this.bot = bot;
    }

    public abstract void reply(Long chatId, Map<String, Integer> messageIds);
}