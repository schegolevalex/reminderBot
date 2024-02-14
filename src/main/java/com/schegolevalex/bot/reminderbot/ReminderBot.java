package com.schegolevalex.bot.reminderbot;

import com.schegolevalex.bot.reminderbot.config.BotConfiguration;
import com.schegolevalex.bot.reminderbot.config.Constant;
import com.schegolevalex.bot.reminderbot.entity.Reminder;
import com.schegolevalex.bot.reminderbot.service.ReminderService;
import com.schegolevalex.bot.reminderbot.state.AbstractState;
import com.schegolevalex.bot.reminderbot.state.State;
import jakarta.annotation.PostConstruct;
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

import static com.schegolevalex.bot.reminderbot.config.Constant.Command;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReminderBot extends TelegramWebhookBot {
    private final BotConfiguration botConfiguration;
    private final List<AbstractState> states;
    private final Map<Long, ChatContext> context = new HashMap<>();
    private final ReminderService reminderService;
    private final ThreadPoolTaskScheduler taskScheduler;

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        log.info("Received update: {}", update);
        Long chatId = AbilityUtils.getChatId(update);

        if (update.hasMessage() && update.getMessage().hasText() && update.getMessage().getText().equals(Command.START))
            getStateStack(chatId).clear();

        peekState(chatId).perform(update);
        executeReply(chatId, peekState(chatId).reply(update));
        deleteUserMessage(update);

        return null;
    }

    private void executeReply(Long chatId, CustomReply reply) {
        Integer messageIdToEdit = this.context.get(chatId).getMessageIdToEdit();

        try {
            if (messageIdToEdit == null) {
                SendMessage sendMessage = SendMessage.builder()
                        .chatId(chatId)
                        .text(reply.text())
                        .replyMarkup(reply.replyMarkup())
                        .build();
                Message executedMessage = execute(sendMessage);
                this.context.get(chatId).setMessageIdToEdit(executedMessage.getMessageId());
            } else {
                EditMessageText editMessageText = EditMessageText.builder()
                        .chatId(chatId)
                        .messageId(messageIdToEdit)
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
        long chatId = reminder.getChatId();
        UUID reminderId = reminder.getId();

        if (this.context.containsKey(chatId) && this.context.get(chatId).getFutureMap().containsKey(reminderId))
            this.context.get(chatId).getFutureMap().get(reminderId).cancel(false);

        if (remindAt.isAfter(Instant.now())) {
            Runnable task = () -> {
                pushState(chatId, State.SHOW_REMINDER);
                try {
                    Message executed = execute(SendMessage.builder()
                            .chatId(chatId)
                            .text(String.format(Constant.Message.REMINDER_MESSAGE, reminder.getTime(), reminder.getText()))
                            .replyMarkup(KeyboardFactory.withOkButton())
                            .build());
                    this.context.get(chatId).setMessageIdToDelete(executed.getMessageId());
                    this.context.get(chatId).getFutureMap().remove(reminderId);
                } catch (TelegramApiException e) {
                    log.error("*********** Не удалось отправить сообщение ************"); //todo
                }
            };

            this.context.get(chatId).getFutureMap().put(reminderId, taskScheduler.schedule(task, remindAt));
        }
    }

    private Stack<AbstractState> getStateStack(Long chatId) {
        Stack<AbstractState> stateStack;
        if (context.get(chatId) == null || context.get(chatId).getStateStack() == null) {
            stateStack = new Stack<>();
            stateStack.push(findState(State.AWAIT_START));
            context.put(chatId, new ChatContext(stateStack));
        } else if (context.get(chatId).getStateStack().isEmpty()) {
            stateStack = context.get(chatId).getStateStack();
            stateStack.push(findState(State.AWAIT_START));
        } else
            stateStack = context.get(chatId).getStateStack();
        return stateStack;
    }

    public void pushState(Long chatId, State stateType) {
        if (peekState(chatId).getType() != stateType) {
            getStateStack(chatId).push(findState(stateType));
        }
    }

    public AbstractState peekState(Long chatId) {
        return getStateStack(chatId).peek();
    }

    public void popState(Long chatId) {
        getStateStack(chatId).pop();
    }

    private AbstractState findState(State stateType) {
        return this.states.stream()
                .filter(state -> state.getType() == stateType)
                .findAny()
                .orElseThrow(RuntimeException::new);
    }

    public ChatContext getChatContext(Long chatId) {
        return context.get(chatId);
    }

    @PostConstruct
    private void registerPreviousReminders() {
        reminderService.getAllReminders().forEach(this::remind); // todo разобраться со временем и вытащить из базы сразу только то, что нужно
    }

    private void deleteUserMessage(Update update) {
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
