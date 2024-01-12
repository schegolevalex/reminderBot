package com.schegolevalex.bot.reminderbot;

import com.schegolevalex.bot.reminderbot.configs.BotConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.ApiConstants;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.updates.DeleteWebhook;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class ReminderBot extends TelegramWebhookBot {

    private final BotConfiguration botConfiguration;

    @Autowired
    public ReminderBot(BotConfiguration botConfiguration) {
        this.botConfiguration = botConfiguration;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        return null;
    }

//    public void deleteMessage(Update update) {
//        String chatId = String.valueOf(AbilityUtils.getChatId(update));
//        Integer messageId = update.getMessage().getMessageId();
//
//        DeleteMessage deleteMessage = new DeleteMessage();
//        deleteMessage.setChatId(chatId);
//        deleteMessage.setMessageId(messageId);
//
//        try {
//            execute(deleteMessage);
//        } catch (TelegramApiException e) {
//            e.printStackTrace(); //todo залогировать ошибку и отправить пользователю сообщение, что произошла ошибка
//        }
//    }

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
        SetWebhook setWebhook = new SetWebhook(ApiConstants.BASE_URL + botConfiguration.getBotToken()
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
