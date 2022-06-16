package com.schegolevalex.bot.reminderbot.states;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.util.AbilityUtils;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;
import java.util.Stack;

@Component
public class ReminderFacade {

    private final Map<Long, Stack<UserState>> userStates;
    private final AwaitingStartState awaitingStartState;

    @Autowired
    public ReminderFacade(Map<Long, Stack<UserState>> userStates,
                          AwaitingStartState awaitingStartState) {
        this.userStates = userStates;
        this.awaitingStartState = awaitingStartState;
    }

    public BotApiMethod<?> getResult(Update update) {
        Long chatId = AbilityUtils.getChatId(update);

        process(update);
        return getReply(chatId);
    }

    public void process(Update update) {
        Long chatId = AbilityUtils.getChatId(update);
        Stack<UserState> userState = getCurrentState(chatId);
        userStates.get(chatId).peek().process(update, userState);
    }

    public SendMessage getReply(Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        Stack<UserState> userState = getCurrentState(chatId);
        return userState.peek().setText(sendMessage);
    }

    public SendMessage getReply(Long chatId, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(text);
        return sendMessage;
    }

    protected Stack<UserState> getCurrentState(Long chatId) {
        Stack<UserState> userState;

        if (userStates.get(chatId) != null) {
            userState = userStates.get(chatId);
        } else {
            userState = new Stack<>();
            userState.push(awaitingStartState);
            userStates.put(chatId, userState);
        }
        return userState;
    }

    // нужны ли эти методы, если мы можем в самих состояниях менять по ссылке? TODO
    public void setNextState(UserState userState) {

    }

    public void setPreviousState() {

    }
}
