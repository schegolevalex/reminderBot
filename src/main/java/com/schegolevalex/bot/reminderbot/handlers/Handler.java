package com.schegolevalex.bot.reminderbot.handlers;

import com.schegolevalex.bot.reminderbot.states.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Update;
import java.util.Stack;

public abstract class Handler {
    protected final AwaitingStartState awaitingStartState;
    protected final ChoosingFirstActionState choosingFirstActionState;
    protected final WatchingRemindersState watchingRemindersState;
    protected final AddingReminderTextState addingReminderTextState;
    protected final AddingReminderDateState addingReminderDateState;
    protected final AddingReminderTimeState addingReminderTimeState;
    protected final WrongInputState wrongInputState;

    @Autowired
    protected Handler(AwaitingStartState awaitingStartState,
                      ChoosingFirstActionState choosingFirstActionState,
                      WatchingRemindersState watchingRemindersState,
                      AddingReminderTextState addingReminderTextState,
                      AddingReminderDateState addingReminderDateState,
                      AddingReminderTimeState addingReminderTimeState,
                      WrongInputState wrongInputState) {
        this.awaitingStartState = awaitingStartState;
        this.choosingFirstActionState = choosingFirstActionState;
        this.watchingRemindersState = watchingRemindersState;
        this.addingReminderTextState = addingReminderTextState;
        this.addingReminderDateState = addingReminderDateState;
        this.addingReminderTimeState = addingReminderTimeState;
        this.wrongInputState = wrongInputState;
    }

    public abstract void handle(Update update, Stack<UserState> userState);
}
