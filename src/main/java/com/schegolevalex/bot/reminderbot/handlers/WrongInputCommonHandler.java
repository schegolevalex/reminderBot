package com.schegolevalex.bot.reminderbot.handlers;

import com.schegolevalex.bot.reminderbot.repliers.AbstractReplier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Stack;

@Component
public class WrongInputCommonHandler extends Handler {

    @Override
    public void handle(Update update, Stack<AbstractReplier> replierStack) {
        replierStack.push(replierMap.get("wrongInputCommonReplier"));
    }
}
