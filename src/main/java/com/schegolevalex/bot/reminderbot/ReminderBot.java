package com.schegolevalex.bot.reminderbot;

import com.schegolevalex.bot.reminderbot.configs.BotConfiguration;
import com.schegolevalex.bot.reminderbot.entities.Reminder;
import com.schegolevalex.bot.reminderbot.services.ReminderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.DeleteWebhook;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class ReminderBot extends TelegramWebhookBot {

    private final BotConfiguration botConfiguration;
    private final ReminderFacade reminderFacade;
    private final ThreadPoolTaskScheduler taskScheduler;
    private final ReminderService reminderService;

    @Autowired
    public ReminderBot(BotConfiguration botConfiguration,
                       ReminderFacade reminderFacade,
                       ThreadPoolTaskScheduler taskScheduler,
                       ReminderService reminderService) {
        this.botConfiguration = botConfiguration;
        this.reminderFacade = reminderFacade;
        this.taskScheduler = taskScheduler;
        this.reminderService = reminderService;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        BotApiMethod<?> result = reminderFacade.getResult(update);
        executeMethod(result);
        return result;
    }

    public void sendReminder(Reminder reminder) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reminderDateTime = reminder.getDate().atTime(reminder.getTime());

        if (now.isBefore(reminderDateTime)) {
            Date date = Date.from(reminderDateTime.toInstant(ZoneOffset.of("+3")));

            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(reminder.getChatID()));
            message.setText(String.format(Constant.REMINDER_MESSAGE, reminder.getTime(), reminder.getText()));

            taskScheduler.schedule(() -> executeMethod(message), date);
        }
    }

    public void executeMethod(BotApiMethod<?> method) {
        int messageId;
        try {
            if (method instanceof SendMessage) {
                messageId = execute((SendMessage) method).getMessageId();
                reminderFacade.setMessageIds(((SendMessage) method).getChatId(), messageId);
            } else {
                execute(method);
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
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
    private void setOwnWebhook() {
        DeleteWebhook deleteWebhook = new DeleteWebhook();
        deleteWebhook.setDropPendingUpdates(true);
        SetWebhook setWebhook = new SetWebhook("https://api.telegram.org/bot" + botConfiguration.getBotToken()
                + "/" + SetWebhook.PATH + "?url=" + botConfiguration.getWebhookPath());

        try {
            execute(deleteWebhook);
            execute(setWebhook);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @PostConstruct
    private void setMyCommands() {
        List<BotCommand> botCommands = new ArrayList<>();
        botCommands.add(new BotCommand("start", "command for start conversation"));
        SetMyCommands setMyCommands = new SetMyCommands(botCommands, null, null);

        try {
            execute(setMyCommands);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @PostConstruct
    private void registerPreviousReminders() {
        List<Reminder> allReminders = reminderService.getAllReminders();
        for (Reminder reminder : allReminders)
            sendReminder(reminder);
    }
}
