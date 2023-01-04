package com.schegolevalex.bot.reminderbot.handlers;

import com.schegolevalex.bot.reminderbot.states.UserState;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Stack;

@Component
public class WrongInputHandler extends Handler {

    @Override
    public void handle(Update update, Stack<UserState> userStateStack) {
        userStateStack.push(statesMap.get("wrongInputState"));
    }
}
