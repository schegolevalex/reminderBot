package com.schegolevalex.bot.reminderbot.handlers;

import com.schegolevalex.bot.reminderbot.states.UserState;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;
import java.util.Stack;

public abstract class Handler {
    @Autowired
    Map<String, UserState> statesMap;

    abstract void handle(Update update, Stack<UserState> userStateStack);
}
