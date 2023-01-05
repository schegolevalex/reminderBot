package com.schegolevalex.bot.reminderbot;

import com.schegolevalex.bot.reminderbot.states.ChoosingFirstActionState;
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
    private final ChoosingFirstActionState choosingFirstActionState;

    private final Map<String, Integer> messageIds;

    @Autowired
    public ReminderFacade(Map<Long, Stack<UserState>> userStatesMap,
                          ChoosingFirstActionState choosingFirstActionState) {
        this.userStatesMap = userStatesMap;
        this.choosingFirstActionState = choosingFirstActionState;
        messageIds = new HashMap<>();
    }

    public BotApiMethod<?> getResult(Update update) {
        Long chatId = AbilityUtils.getChatId(update);

        process(update);
        return getReply(chatId);
    }

    public void process(Update update) {
        Long chatId = AbilityUtils.getChatId(update);
        Stack<UserState> userState = getCurrentState(chatId);
        userStatesMap.get(chatId).peek().process(update, userState);
    }

    public BotApiMethod<?> getReply(Long chatId) {
        Stack<UserState> userState = getCurrentState(chatId);
        BotApiMethod<?> botApiMethod = userState.peek().getReply(chatId);
        if (botApiMethod instanceof EditMessageText) {
            ((EditMessageText) botApiMethod).setMessageId(messageIds.get(String.valueOf(chatId)));
        }
        return botApiMethod;
    }

    public Stack<UserState> getCurrentState(Long chatId) {
        Stack<UserState> userState;

        if (userStatesMap.get(chatId) != null) {
            userState = userStatesMap.get(chatId);
        } else {
            userState = new Stack<>();
            userState.push(choosingFirstActionState);
            userStatesMap.put(chatId, userState);
        }
        return userState;
    }

    public void setMessageIds(String chatId, Integer messageId) {
        messageIds.put(chatId, messageId);
    }

    public Map<String, Integer> getMessageIds() {
        return messageIds;
    }
}
