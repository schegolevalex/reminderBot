package com.schegolevalex.bot.reminderbot;

import com.schegolevalex.bot.reminderbot.config.BotConfiguration;
import com.schegolevalex.bot.reminderbot.handlers.ResponseHandler;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.updates.DeleteWebhook;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class ReminderBot extends TelegramWebhookBot {

    private final BotConfiguration botConfiguration;
    private final RestTemplate restTemplate;

    private final ResponseHandler responseHandler;

    public ReminderBot(BotConfiguration botConfiguration, RestTemplate restTemplate, ResponseHandler responseHandler) {
        this.botConfiguration = botConfiguration;
        this.restTemplate = restTemplate;
        this.responseHandler = responseHandler;
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
        return responseHandler.onUpdateReceiver(update);
//        String chatId = String.valueOf(update.getMessage().getChatId());
//        String message = update.getMessage().getText();
//
//        SendMessage sendMessage = new SendMessage(chatId, message);
//        return sendMessage;
    }

    @Override
    public String getBotPath() {
        return botConfiguration.getWebhookPath();
    }

    @PostConstruct
    private void setOwnWebhook() {
        restTemplate.getForEntity("https://api.telegram.org/bot" + botConfiguration.getBotToken() +
                "/"+ DeleteWebhook.PATH, String.class);
        restTemplate.getForEntity("https://api.telegram.org/bot" + botConfiguration.getBotToken()
                + "/" + SetWebhook.PATH + "?url=" + botConfiguration.getWebhookPath(), String.class);
    }

    @PostConstruct
    private void setMyCommands() {
        List<BotCommand> botCommands = new ArrayList<>();
        botCommands.add(new BotCommand("start", "command for start conversation"));
        SetMyCommands setMyCommands = new SetMyCommands(botCommands, null, null);

       restTemplate.postForEntity("https://api.telegram.org/bot" + botConfiguration.getBotToken() +
                "/" + SetMyCommands.PATH, setMyCommands, String.class);

    }
}
