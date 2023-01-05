package com.schegolevalex.bot.reminderbot;

import com.schegolevalex.bot.reminderbot.handlers.HandlerFactory;
import com.schegolevalex.bot.reminderbot.states.AwaitStartState;
import com.schegolevalex.bot.reminderbot.states.UserState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.util.AbilityUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

@Component
public class ReminderFacade {

    private final Map<Long, Stack<UserState>> userStatesMap;
    private final AwaitStartState awaitStartState;
    private final HandlerFactory handlerFactory;

    private final Map<String, Integer> messageIds;

    @Autowired
    public ReminderFacade(Map<Long, Stack<UserState>> userStatesMap,
                          AwaitStartState awaitStartState,
                          HandlerFactory handlerFactory) {
        this.userStatesMap = userStatesMap;
        this.awaitStartState = awaitStartState;
        this.handlerFactory = handlerFactory;
        messageIds = new HashMap<>();
    }

    public void perform(Update update) {
        Long chatId = AbilityUtils.getChatId(update);
        Stack<UserState> userStateStack = getCurrentStateStack(chatId);

        handlerFactory.handle(update, userStateStack);
        userStateStack.peek().sendReply(chatId, messageIds);

//        if (botApiMethod instanceof EditMessageText) {
//            ((EditMessageText) botApiMethod).setMessageId(messageIds.get(String.valueOf(chatId)));
//        }
//        return botApiMethod;
    }


    public Stack<UserState> getCurrentStateStack(Long chatId) {
        Stack<UserState> userStateStack;

        if (userStatesMap.get(chatId) == null || userStatesMap.get(chatId).empty()) {
            userStateStack = new Stack<>();
            userStateStack.push(awaitStartState);
            userStatesMap.put(chatId, userStateStack);
        } else userStateStack = userStatesMap.get(chatId);

        return userStateStack;
    }

    public void putToMessageIds(String chatId, Integer messageId) {
        messageIds.put(chatId, messageId);
    }

    public Map<String, Integer> getMessageIds() {
        return messageIds;
    }
}
