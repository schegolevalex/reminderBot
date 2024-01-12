package com.schegolevalex.bot.reminderbot;

import com.schegolevalex.bot.reminderbot.handlers.HandlerFactory;
import com.schegolevalex.bot.reminderbot.repliers.AwaitStartReplier;
import com.schegolevalex.bot.reminderbot.repliers.AbstractReplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.util.AbilityUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

@Component
public class ReminderFacade {

    private final Map<Long, Stack<AbstractReplier>> userStatesMap;
    private final AwaitStartReplier awaitStartReplier;
    private final HandlerFactory handlerFactory;

    private final Map<String, Integer> messageIds;

    @Autowired
    public ReminderFacade(Map<Long, Stack<AbstractReplier>> userStatesMap,
                          AwaitStartReplier awaitStartReplier,
                          HandlerFactory handlerFactory) {
        this.userStatesMap = userStatesMap;
        this.awaitStartReplier = awaitStartReplier;
        this.handlerFactory = handlerFactory;
        messageIds = new HashMap<>();
    }

    public void perform(Update update) {
        Long chatId = AbilityUtils.getChatId(update);
        Stack<AbstractReplier> replierStack = getCurrentStateStack(chatId);

        handlerFactory.handle(update, replierStack);
        System.out.println("*************");
        System.out.println(replierStack.peek());
        System.out.println("*************");
        replierStack.peek().reply(chatId, messageIds);

//        if (botApiMethod instanceof EditMessageText) {
//            ((EditMessageText) botApiMethod).setMessageId(messageIds.get(String.valueOf(chatId)));
//        }
//        return botApiMethod;
    }


    public Stack<AbstractReplier> getCurrentStateStack(Long chatId) {
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

    public Map<String, Integer> getMessageIds() {
        return messageIds;
    }
}
