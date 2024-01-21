package com.schegolevalex.bot.reminderbot;

import com.schegolevalex.bot.reminderbot.config.BotConfiguration;
import com.schegolevalex.bot.reminderbot.config.Constant;
import com.schegolevalex.bot.reminderbot.entity.Reminder;
import com.schegolevalex.bot.reminderbot.service.ReminderService;
import com.schegolevalex.bot.reminderbot.state.AbstractState;
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
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReminderBot extends TelegramWebhookBot {
    private final BotConfiguration botConfiguration;
    private final List<AbstractState> allPossiblesStates;
    private final Map<Long, Stack<AbstractState>> userStatesMap = new HashMap<>();
    @Getter
    private final Map<Long, Reminder> remindersContext = new HashMap<>();
    @Getter
    private final Map<Long, Integer> messagesToEdit = new HashMap<>();
    @Getter
    private final Map<Long, Integer> messagesWithReminderToDelete = new HashMap<>();
    private final ReminderService reminderService;
    private final ThreadPoolTaskScheduler taskScheduler;

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        Long chatId = AbilityUtils.getChatId(update);

        peekState(chatId).perform(update);
        CustomReply reply = peekState(chatId).reply(update);

        executeReply(chatId, reply);

        executeDeleteUserMessage(update);
        return null;
    }

    private void executeReply(Long chatId, CustomReply reply) {
        Integer messageToEditId = this.messagesToEdit.get(chatId);

        try {
            if (messageToEditId == null) {
                SendMessage sendMessage = SendMessage.builder()
                        .chatId(chatId)
                        .text(reply.text())
                        .replyMarkup(reply.replyMarkup())
                        .build();
                Message executedMessage = execute(sendMessage);
                this.messagesToEdit.put(chatId, executedMessage.getMessageId());
            } else {
                EditMessageText editMessageText = EditMessageText.builder()
                        .chatId(chatId)
                        .messageId(messageToEditId)
                        .text(reply.text())
                        .replyMarkup(reply.replyMarkup())
                        .build();
                execute(editMessageText);
            }
        } catch (TelegramApiException e) {
            log.error("*********** Не удалось отправить сообщение ************"); //todo
        }
    }

    public void remind(Reminder reminder) {
        Instant remindAt = reminder.getDate().atTime(reminder.getTime()).toInstant(ZoneOffset.ofHours(3));

        if (remindAt.isAfter(Instant.now())) {
            taskScheduler.schedule(() -> {
                long chatId = reminder.getChatId();
                pushState(chatId, State.SHOW_REMINDER);
                SendMessage sendMessage = SendMessage.builder()
                        .chatId(chatId)
                        .text(String.format(Constant.Message.REMINDER_MESSAGE, reminder.getTime(), reminder.getText()))
                        .replyMarkup(KeyboardFactory.withOkButton())
                        .build();
                try {
                    Message executed = execute(sendMessage);
                    messagesWithReminderToDelete.put(chatId, executed.getMessageId());
                } catch (TelegramApiException e) {
                    log.error("*********** Не удалось отправить сообщение ************"); //todo
                }
            }, remindAt);
        }
    }

    private Stack<AbstractState> getStateStack(Long chatId) {
        Stack<AbstractState> stateStack;
        if (userStatesMap.get(chatId) == null || userStatesMap.get(chatId).empty()) {
            stateStack = new Stack<>();
            stateStack.push(findState(State.AWAIT_START));
            userStatesMap.put(chatId, stateStack);
        } else
            stateStack = userStatesMap.get(chatId);

        return stateStack;
    }

    public void pushState(Long chatId, State stateType) {
        if (stateType == State.CHOOSE_FIRST_ACTION)
            getStateStack(chatId).clear();
        if (peekState(chatId).getType() != stateType) {
            getStateStack(chatId).push(findState(stateType));
        }
    }

    private AbstractState peekState(Long chatId) {
        return getStateStack(chatId).peek();
    }

    public void popState(Long chatId) {
        getStateStack(chatId).pop();
    }

    private AbstractState findState(State stateType) {
        return this.allPossiblesStates.stream()
                .filter(state -> state.getType() == stateType)
                .findAny()
                .orElseThrow(RuntimeException::new);
    }

    @PostConstruct
    private void registerPreviousReminders() {
        reminderService.getAllReminders().forEach(this::remind); // todo разобраться со временем и вытащить из базы сразу только то, что нужно
    }

    private void executeDeleteUserMessage(Update update) {
        try {
            if (update.hasMessage())
                execute(DeleteMessage.builder()
                        .chatId(AbilityUtils.getChatId(update))
                        .messageId(update.getMessage().getMessageId())
                        .build());
        } catch (TelegramApiException e) {
            log.error("*********** Не удалось удалить сообщение ************"); //todo
        }
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
        botCommands.add(new BotCommand("start", Constant.Message.START_COMMAND_DESCRIPTION));
        execute(new SetMyCommands(botCommands, null, null));
    }

    @PostConstruct
    @SneakyThrows
    private void setStartDescription() {
        execute(new SetMyDescription(Constant.Message.START_DESCRIPTION, "ru"));
    }
}
