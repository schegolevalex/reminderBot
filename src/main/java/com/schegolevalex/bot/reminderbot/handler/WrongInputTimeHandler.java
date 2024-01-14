package com.schegolevalex.bot.reminderbot.handler;

import com.schegolevalex.bot.reminderbot.state.AbstractState;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Stack;

@Component
public class WrongInputTimeHandler extends Handler {

    @Override
    public void handle(Update update, Stack<AbstractState> stateStack) {
        stateStack.push(replierMap.get("wrongInputTimeReplier"));
    }
}
