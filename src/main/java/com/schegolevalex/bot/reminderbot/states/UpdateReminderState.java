package com.schegolevalex.bot.reminderbot.states;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

public class UpdateReminderState implements UserState{
    @Override
    public BotApiMethod<?> getReply(Long chatId) {
        return null; // todo
    }
}