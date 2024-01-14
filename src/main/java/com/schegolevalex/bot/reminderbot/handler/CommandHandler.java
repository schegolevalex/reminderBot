package com.schegolevalex.bot.reminderbot.handler;

import com.schegolevalex.bot.reminderbot.ReminderBot;
import com.schegolevalex.bot.reminderbot.state.AbstractState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.util.AbilityUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;

@Component
public class CommandHandler extends Handler {

    @Autowired
    protected CommandHandler(Map<String, AbstractState> stateMap) {
        super(stateMap);
    }

    @Override
    public void handle(Update update, ReminderBot bot) {

    }
}
