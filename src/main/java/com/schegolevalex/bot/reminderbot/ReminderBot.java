package com.schegolevalex.bot.reminderbot;

import com.schegolevalex.bot.reminderbot.config.BotConfiguration;
import com.schegolevalex.bot.reminderbot.entity.Reminder;
import com.schegolevalex.bot.reminderbot.state.AbstractState;
import com.schegolevalex.bot.reminderbot.state.AwaitStartState;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.util.AbilityUtils;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.ApiConstants;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.DeleteWebhook;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

@Component
@RequiredArgsConstructor
public class ReminderBot extends TelegramWebhookBot {

    private final BotConfiguration botConfiguration;
    private final Map<Long, Stack<AbstractState>> userStatesMap = new HashMap<>();
    private final AwaitStartState awaitStartState;
    @Getter
    private final Map<Long, Reminder> tempReminders = new HashMap<>();

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        Long chatId = AbilityUtils.getChatId(update);
        getBotState(chatId).handle(update);
        return getBotState(chatId).reply(update);
    }

    private Stack<AbstractState> getBotStateStack(Long chatId) {
        Stack<AbstractState> stateStack;

        if (userStatesMap.get(chatId) == null || userStatesMap.get(chatId).empty()) {
            stateStack = new Stack<>();
            stateStack.push(awaitStartState);
            userStatesMap.put(chatId, stateStack);
        } else stateStack = userStatesMap.get(chatId);

        return stateStack;
    }

    public AbstractState getBotState(Long chatId) {
        return getBotStateStack(chatId).peek();
    }

    public void pushBotState(Long chatId, AbstractState state) {
        getBotStateStack(chatId).push(state);
    }

    public void popBotState(Long chatId) {
        getBotStateStack(chatId).pop();
    }

    @SneakyThrows
    public void sendMessage(long chatId, String textMessage) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(textMessage);
        execute(sendMessage);
    }

    @SneakyThrows
    public void sendMessage(SendMessage sendMessage) {
        execute(sendMessage);
    }

    @Override
    public String getBotUsername() {
        return botConfiguration.getUsername();
    }

    @Override
    public String getBotToken() {
        return botConfiguration.getBotToken();
    }

    @Override
    public String getBotPath() {
        return botConfiguration.getWebhookPath();
    }

    @PostConstruct
    private void setOwnWebhook() {
        DeleteWebhook deletePreviousWebhook = new DeleteWebhook();
        deletePreviousWebhook.setDropPendingUpdates(true);
        SetWebhook setNewWebhook = new SetWebhook(ApiConstants.BASE_URL + botConfiguration.getBotToken()
                + "/" + SetWebhook.PATH + "?url=" + botConfiguration.getWebhookPath());

        try {
            execute(deletePreviousWebhook);
            execute(setNewWebhook);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @PostConstruct
    private void setMyCommands() {
        List<BotCommand> botCommands = new ArrayList<>();
        botCommands.add(new BotCommand("start", "нажми для начала общения"));

        try {
            execute(new SetMyCommands(botCommands, null, null));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
