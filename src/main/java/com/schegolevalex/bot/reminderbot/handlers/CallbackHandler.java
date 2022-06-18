package com.schegolevalex.bot.reminderbot.handlers;

import com.schegolevalex.bot.reminderbot.Constant;
import com.schegolevalex.bot.reminderbot.states.UserState;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Stack;

@Component
public class CallbackHandler extends Handler {

    @Override
    public void handle(Update update, Stack<UserState> userState) {

        switch (update.getCallbackQuery().getData()) {
            case (Constant.GO_TO_MY_REMINDERS):
                userState.push(states.get("watchingRemindersState"));
                break;

            case (Constant.GO_TO_ADD_REMINDER):
                userState.push(states.get("addingReminderTextState"));
                break;

            case (Constant.GO_BACK):
                userState.pop();
                break;

            default:
                userState.push(states.get("wrongInputState"));
                break;
        }
    }
}
