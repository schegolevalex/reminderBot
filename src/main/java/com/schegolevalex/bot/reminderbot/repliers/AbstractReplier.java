package com.schegolevalex.bot.reminderbot.repliers;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

@Component
public abstract class AbstractReplier {
    public abstract BotApiMethod<?> reply(Long chatId);
}