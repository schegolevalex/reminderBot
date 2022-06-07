package com.schegolevalex.bot.reminderbot.handlers;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Stack;

public interface Handler {
    BotApiMethod<?> handle(Update update, Stack<UserState> userState);
}
