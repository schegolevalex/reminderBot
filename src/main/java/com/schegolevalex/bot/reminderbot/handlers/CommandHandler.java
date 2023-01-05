package com.schegolevalex.bot.reminderbot.handlers;

import com.schegolevalex.bot.reminderbot.states.UserState;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Stack;

@Component
public class CommandHandler extends Handler {

    @Override
    public void handle(Update update, Stack<UserState> userStates) {
        switch (update.getMessage().getText()) {
            case ("/start"):
                userStates.push(statesMap.get("choosingFirstActionState"));
                break;

            default:
                userStates.push(statesMap.get("wrongInputState"));
                break;
        }
    }
}
