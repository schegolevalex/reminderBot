package com.schegolevalex.bot.reminderbot;

import com.schegolevalex.bot.reminderbot.handlers.HandlerFactory;
import com.schegolevalex.bot.reminderbot.repliers.AbstractReplier;
import com.schegolevalex.bot.reminderbot.repliers.AwaitStartReplier;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.util.AbilityUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

@Component
@RequiredArgsConstructor
public class ReminderService {

    private final Map<Long, Stack<AbstractReplier>> userStatesMap;
    private final AwaitStartReplier awaitStartReplier;
    private final HandlerFactory handlerFactory;

    @Getter
    private final Map<String, Integer> messageIds = new HashMap<>();

    public void perform(Update update) {
        Long chatId = AbilityUtils.getChatId(update);
        Stack<AbstractReplier> replierStack = getCurrentStateStack(chatId);

        handlerFactory.handle(update, replierStack);
        System.out.println(replierStack.peek());
        replierStack.peek().reply(chatId, messageIds);
    }

    private Stack<AbstractReplier> getCurrentStateStack(Long chatId) {
        Stack<AbstractReplier> replierStack;

        if (userStatesMap.get(chatId) == null || userStatesMap.get(chatId).empty()) {
            replierStack = new Stack<>();
            replierStack.push(awaitStartReplier);
            userStatesMap.put(chatId, replierStack);
        } else replierStack = userStatesMap.get(chatId);

        return replierStack;
    }

    public void putToMessageIds(String chatId, Integer messageId) {
        messageIds.put(chatId, messageId);
    }

}
