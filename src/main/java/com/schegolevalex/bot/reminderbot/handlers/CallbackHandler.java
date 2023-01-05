package com.schegolevalex.bot.reminderbot.handlers;

import com.schegolevalex.bot.reminderbot.Constant;
import com.schegolevalex.bot.reminderbot.states.UserState;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Stack;

@Component
public class CallbackHandler extends Handler {

    @Override
    public void handle(Update update, Stack<UserState> userStateStack) {

        switch (update.getCallbackQuery().getData()) {
            case (Constant.GO_TO_MY_REMINDERS) -> userStateStack.push(statesMap.get("watchRemindersState"));
            case (Constant.GO_TO_ADD_REMINDER) -> userStateStack.push(statesMap.get("addReminderTextState"));
            case (Constant.GO_TO_UPDATE_REMINDER) -> userStateStack.push(statesMap.get("updateReminderState"));
            case (Constant.GO_TO_DELETE_REMINDER) -> userStateStack.push(statesMap.get("deleteReminderState"));
            case (Constant.GO_BACK) -> userStateStack.pop();
            default -> userStateStack.push(statesMap.get("wrongInputState"));
        }
    }
}
