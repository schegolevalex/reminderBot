package com.schegolevalex.bot.reminderbot.handlers;

import com.schegolevalex.bot.reminderbot.Constant;
import com.schegolevalex.bot.reminderbot.states.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Stack;

@Component
public class CallbackHandler extends Handler {

    @Autowired
    protected CallbackHandler(AwaitingStartState awaitingStartState,
                              ChoosingFirstActionState choosingFirstActionState,
                              WatchingRemindersState watchingRemindersState,
                              AddingReminderTextState addingReminderTextState,
                              AddingReminderDateState addingReminderDateState,
                              AddingReminderTimeState addingReminderTimeState,
                              WrongInputState wrongInputState) {
        super(awaitingStartState, choosingFirstActionState, watchingRemindersState,
                addingReminderTextState, addingReminderDateState, addingReminderTimeState, wrongInputState);
    }

    @Override
    public void handle(Update update, Stack<UserState> userState) {

        switch (update.getCallbackQuery().getData()) {
            case (Constant.GO_TO_MY_REMINDERS):
                userState.push(watchingRemindersState);
                break;

            case (Constant.GO_TO_ADD_REMINDER):
                userState.push(addingReminderTextState);
                break;

            case (Constant.GO_BACK):
                userState.pop();
                break;

            default:
                userState.push(wrongInputState);
                break;
        }
    }
}
