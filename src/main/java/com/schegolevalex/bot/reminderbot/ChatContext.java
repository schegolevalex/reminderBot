package com.schegolevalex.bot.reminderbot;

import com.schegolevalex.bot.reminderbot.entity.Reminder;
import com.schegolevalex.bot.reminderbot.state.AbstractState;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Stack;

@Getter
@Setter
@RequiredArgsConstructor
public class ChatContext {
    private final Stack<AbstractState> stateStack;
    private Reminder tempReminder;
    private Integer messageIdToEdit;
    private Integer messageIdToDelete;
}
