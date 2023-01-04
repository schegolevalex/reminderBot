package com.schegolevalex.bot.reminderbot.states;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

public interface UserState {
    BotApiMethod<?> getReply(Long chatId);
}