package com.schegolevalex.bot.reminderbot;

import com.schegolevalex.bot.reminderbot.configs.BotConfiguration;
import com.schegolevalex.bot.reminderbot.entities.Reminder;
import com.schegolevalex.bot.reminderbot.handlers.UpdateReceiver;
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
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class ReminderBot extends TelegramWebhookBot {

    private final BotConfiguration botConfiguration;
    private final UpdateReceiver updateReceiver;
    private final ThreadPoolTaskScheduler taskScheduler;

    @Autowired
    public ReminderBot(BotConfiguration botConfiguration,
                       UpdateReceiver updateReceiver,
                       ThreadPoolTaskScheduler taskScheduler) {
        this.botConfiguration = botConfiguration;
        this.updateReceiver = updateReceiver;
        this.taskScheduler = taskScheduler;
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
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        return updateReceiver.receive(update);
    }

    public void sendReminder(Reminder reminder) {

        Date date = Date.from(reminder.getDate().atTime(reminder.getTime()).toInstant(ZoneOffset.of("+3")));

        taskScheduler.schedule(() ->
                sendMessage(String.format(Constant.REMINDER_MESSAGE, reminder.getTime(), reminder.getText()),
                        reminder.getChatID()), date);
    }

    public void sendMessage(String text, Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(text);
        sendMessage.setChatId(String.valueOf(chatId));
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
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
}
