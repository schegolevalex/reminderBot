package com.schegolevalex.bot.reminderbot.handlers;

import com.schegolevalex.bot.reminderbot.repliers.AbstractReplier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;
import java.util.Stack;

@Component
@RequiredArgsConstructor
public class HandlerFactory {

    private final Map<String, Handler> handlerMap;

    public void handle(Update update, Stack<AbstractReplier> replierStack) {

        if (replierStack.peek().getClass().getSimpleName().equals("WrongInputDateReplier")) {
            replierStack.pop();
            handlerMap.get("wrongInputDateHandler").handle(update, replierStack);
        } else if (replierStack.peek().getClass().getSimpleName().equals("WrongInputTimeReplier")) {
            replierStack.pop();
            handlerMap.get("wrongInputTimeHandler").handle(update, replierStack);
        } else if (update.hasCallbackQuery())
            handlerMap.get("callbackHandler").handle(update, replierStack);
        else if (update.hasMessage() && update.getMessage().isCommand())
            handlerMap.get("commandHandler").handle(update, replierStack);
        else if (update.hasMessage() && update.getMessage().hasText())
            handlerMap.get("textHandler").handle(update, replierStack);
        else handlerMap.get("wrongInputCommonHandler").handle(update, replierStack);
    }
}
