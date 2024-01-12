package com.schegolevalex.bot.reminderbot.handlers;

import com.schegolevalex.bot.reminderbot.repliers.AbstractReplier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Stack;

@Component
public class CommandHandler extends Handler {

    @Override
    public void handle(Update update, Stack<AbstractReplier> replierStack) {
        switch (update.getMessage().getText()) {
            case ("/start") -> replierStack.push(replierMap.get("chooseFirstActionReplier"));
            default -> replierStack.push(replierMap.get("wrongInputReplier"));
        }
    }
}
