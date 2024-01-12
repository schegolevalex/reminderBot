package com.schegolevalex.bot.reminderbot.handlers;

import com.schegolevalex.bot.reminderbot.repliers.AbstractReplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;
import java.util.Stack;

public abstract class Handler {
    @Autowired
    Map<String, AbstractReplier> replierMap;

    abstract void handle(Update update, Stack<AbstractReplier> replierStack);
}
