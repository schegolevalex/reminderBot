package com.schegolevalex.bot.reminderbot;

import com.schegolevalex.bot.reminderbot.handlers.HandlerFactory;
import com.schegolevalex.bot.reminderbot.states.AwaitingStartState;
import com.schegolevalex.bot.reminderbot.states.UserState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.util.AbilityUtils;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

@Component
public class ReminderFacade {

    private final Map<Long, Stack<UserState>> userStatesMap;
    private final AwaitingStartState awaitingStartState;
    private final HandlerFactory handlerFactory;

    private final Map<String, Integer> messageIds;

    @Autowired
    public ReminderFacade(Map<Long, Stack<UserState>> userStatesMap,
                          AwaitingStartState awaitingStartState,
                          HandlerFactory handlerFactory) {
        this.userStatesMap = userStatesMap;
        this.awaitingStartState = awaitingStartState;
        this.handlerFactory = handlerFactory;
        messageIds = new HashMap<>();
    }

    public BotApiMethod<?> getResult(Update update) {
        process(update);
        return getBotApiMethod(AbilityUtils.getChatId(update));
    }

    private void process(Update update) {
        Long chatId = AbilityUtils.getChatId(update);
        Stack<UserState> userStateStack = getCurrentStateStack(chatId);
        handlerFactory.handle(update, userStateStack);
    }

    private BotApiMethod<?> getBotApiMethod(Long chatId) {
        Stack<UserState> userState = getCurrentStateStack(chatId);
        BotApiMethod<?> botApiMethod = userState.peek().getReply(chatId);
        if (botApiMethod instanceof EditMessageText) {
            ((EditMessageText) botApiMethod).setMessageId(messageIds.get(String.valueOf(chatId)));
        }
        return botApiMethod;
    }

    public Stack<UserState> getCurrentStateStack(Long chatId) {
        Stack<UserState> userStateStack;

        if (userStatesMap.get(chatId) == null) {
            userStateStack = new Stack<>();
            userStateStack.push(awaitingStartState);
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
