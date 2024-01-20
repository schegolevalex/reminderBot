package com.schegolevalex.bot.reminderbot;

import com.schegolevalex.bot.reminderbot.config.BotConfiguration;
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
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChat;
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
    private final Map<Long, Reminder> remindersContext = new HashMap<>();
    @Getter
    private final Map<Long, Integer> messagesToEdit = new HashMap<>();
    private final ReminderService reminderService;
    private final ThreadPoolTaskScheduler taskScheduler;

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        Long chatId = AbilityUtils.getChatId(update);

        peekBotState(chatId).perform(update);
        CustomReply reply = peekBotState(chatId).reply(update);

        executeReply(chatId, reply);

        if (update.hasMessage() && update.getMessage().hasText())
            deleteUserMessage(update);
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

    private Stack<AbstractState> getBotStateStack(Long chatId) {
        Stack<AbstractState> stateStack;
        if (userStatesMap.get(chatId) == null || userStatesMap.get(chatId).empty()) {
            stateStack = new Stack<>();
            stateStack.push(findState(State.AWAIT_START));
            userStatesMap.put(chatId, stateStack);
        } else
            stateStack = userStatesMap.get(chatId);

        return stateStack;
    }

    public void pushBotState(Long chatId, State stateType) {
        if (stateType == State.CHOOSE_FIRST_ACTION)
            getBotStateStack(chatId).clear();
        if (peekBotState(chatId).getType() != stateType) {
            getBotStateStack(chatId).push(findState(stateType));
        }
    }

    private AbstractState peekBotState(Long chatId) {
        return getBotStateStack(chatId).peek();
    }

    public void popBotState(Long chatId) {
        getBotStateStack(chatId).pop();
    }

    private AbstractState findState(State stateType) {
        return this.allPossiblesStates.stream()
                .filter(state -> state.getType() == stateType)
                .findAny()
                .orElseThrow(RuntimeException::new);
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

            SendMessage message = SendMessage.builder()
                    .chatId(reminder.getChatId())
                    .text(String.format(Constant.Message.REMINDER_MESSAGE, reminder.getTime(), reminder.getText()))
                    .build();

            taskScheduler.schedule(() -> {
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    log.error("************************** Unable to remind ***************************");
                }
            }, instant);
        }
    }

    private void deleteUserMessage(Update update) {
        try {
            execute(DeleteMessage.builder()
                    .chatId(AbilityUtils.getChatId(update))
                    .messageId(update.getMessage().getMessageId())
                    .build());
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
