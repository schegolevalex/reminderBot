package com.schegolevalex.bot.reminderbot;

import com.schegolevalex.bot.reminderbot.config.BotConfiguration;
import com.schegolevalex.bot.reminderbot.entity.Reminder;
import com.schegolevalex.bot.reminderbot.services.ReminderService;
import com.schegolevalex.bot.reminderbot.state.AbstractState;
import com.schegolevalex.bot.reminderbot.state.AwaitStartState;
import com.schegolevalex.bot.reminderbot.state.State;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.util.AbilityUtils;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.ApiConstants;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.description.SetMyDescription;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.DeleteWebhook;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReminderBot extends TelegramWebhookBot {

    private final BotConfiguration botConfiguration;
    private final Map<Long, Stack<AbstractState>> userStatesMap = new HashMap<>();
    private final List<AbstractState> allPossiblesStates;
    @Getter
    private final Map<Long, Reminder> tempReminders = new HashMap<>();
    //    @Getter
//    private final Map<Long, Reminder> messages = new HashMap<>(); //todo
    private final ReminderService reminderService;
    private final ThreadPoolTaskScheduler taskScheduler;

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        Long chatId = AbilityUtils.getChatId(update);
        getBotState(chatId).perform(update);
        try {
            execute(getBotState(chatId).reply(update));
        } catch (TelegramApiException e) {
            e.printStackTrace();
            log.error("*********** Не удалось отправить сообщение ************"); //todo
        }
        return null;
    }

    private Stack<AbstractState> getBotStateStack(Long chatId) {
        Stack<AbstractState> stateStack;

        if (userStatesMap.get(chatId) == null || userStatesMap.get(chatId).empty()) {
            stateStack = new Stack<>();
            stateStack.push(new AwaitStartState(this));
            userStatesMap.put(chatId, stateStack);
        } else stateStack = userStatesMap.get(chatId);

        return stateStack;
    }

    private AbstractState getBotState(Long chatId) {
        return getBotStateStack(chatId).peek();
    }

    public void pushBotState(Long chatId, State stateType) {
        if (getBotState(chatId).getType() != stateType) {
            getBotStateStack(chatId).push(this.allPossiblesStates.stream()
                    .filter(state -> state.getType() == stateType)
                    .findAny()
                    .orElseThrow(RuntimeException::new));
        }
    }

    public void popBotState(Long chatId) {
        getBotStateStack(chatId).pop();
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
    @SneakyThrows
    private void setOwnWebhook() {
        DeleteWebhook deletePreviousWebhook = new DeleteWebhook();
        deletePreviousWebhook.setDropPendingUpdates(true);
        SetWebhook setNewWebhook = new SetWebhook(ApiConstants.BASE_URL + botConfiguration.getBotToken()
                + "/" + SetWebhook.PATH + "?url=" + botConfiguration.getWebhookPath());

        execute(deletePreviousWebhook);
        execute(setNewWebhook);
    }

    @PostConstruct
    @SneakyThrows
    private void setMyCommands() {
        List<BotCommand> botCommands = new ArrayList<>();
        botCommands.add(new BotCommand("start", "нажми для начала общения"));
        execute(new SetMyCommands(botCommands, null, null));
    }

    @PostConstruct
    @SneakyThrows
    private void setStartDescription() {
        execute(new SetMyDescription(Constant.START_DESCRIPTION, "ru"));
    }

    @PostConstruct
    private void registerPreviousReminders() {
        List<Reminder> allReminders = reminderService.getAllReminders();
        for (Reminder reminder : allReminders)
            remind(reminder);
    }

    public void remind(Reminder reminder) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reminderDateTime = reminder.getDate().atTime(reminder.getTime());

        if (reminderDateTime.isAfter(now)) {
            Instant instant = reminderDateTime.toInstant(ZoneOffset.of("+3"));

            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(reminder.getChatId()));
            message.setText(String.format(Constant.REMINDER_MESSAGE, reminder.getTime(), reminder.getText()));

            taskScheduler.schedule(() -> {
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    log.error("************************** Unable to send reminder ***************************");
                }
            }, instant);
        }
    }
}
