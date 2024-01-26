package com.schegolevalex.bot.reminderbot;

import com.schegolevalex.bot.reminderbot.entity.Reminder;
import com.schegolevalex.bot.reminderbot.state.AbstractState;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.UUID;
import java.util.concurrent.Future;

@Getter
@Setter
@RequiredArgsConstructor
public class ChatContext {
    private final Stack<AbstractState> stateStack;
    private Reminder tempReminder;
    private Integer messageIdToEdit;
    private Integer messageIdToDelete;
    private Integer timeInputCursor = 0;
    String[] targetTime = new String[]{"x", "x", "x", "x"};
    Map<UUID, Future<?>> futureMap = new HashMap<>();
}
