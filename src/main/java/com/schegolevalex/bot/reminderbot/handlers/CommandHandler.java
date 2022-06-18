package com.schegolevalex.bot.reminderbot.handlers;

import com.schegolevalex.bot.reminderbot.states.UserState;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Stack;

@Component
public class CommandHandler extends Handler {

    @Override
    public void handle(Update update, Stack<UserState> userState) {
        switch (update.getMessage().getText()) {
            case ("/start"):
                userState.push(states.get("choosingFirstActionState"));
                break;

            default:
                userState.push(states.get("wrongInputState"));
                break;
        }
    }
}
