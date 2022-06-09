package com.schegolevalex.bot.reminderbot.handlers;

import com.schegolevalex.bot.reminderbot.states.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Stack;

@Component
public class WrongInputHandler extends Handler {

    @Autowired
    protected WrongInputHandler(AwaitingStartState awaitingStartState,
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
        userState.push(wrongInputState);
    }
}
