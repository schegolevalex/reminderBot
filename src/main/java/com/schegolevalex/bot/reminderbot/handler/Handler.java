package com.schegolevalex.bot.reminderbot.handler;

import com.schegolevalex.bot.reminderbot.ReminderBot;
import com.schegolevalex.bot.reminderbot.state.AbstractState;
import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;

@Getter
public abstract class Handler {
    private final Map<String, AbstractState> stateMap;

    public Handler(Map<String, AbstractState> stateMap) {
        this.stateMap = stateMap;
    }

    public abstract void handle(Update update, ReminderBot bot);
}
