package com.schegolevalex.bot.reminderbot;

import com.schegolevalex.bot.reminderbot.config.BotConfiguration;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.annotation.PostConstruct;

@Component
public class ReminderBot extends TelegramWebhookBot {

    private final BotConfiguration botConfiguration;
    private final RestTemplate restTemplate;

    public ReminderBot(BotConfiguration botConfiguration, RestTemplate restTemplate) {
        this.botConfiguration = botConfiguration;
        this.restTemplate = restTemplate;
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
        String chatId = String.valueOf(update.getMessage().getChatId());
        String message = update.getMessage().getText();

        SendMessage sendMessage = new SendMessage(chatId, message);
        return sendMessage;
    }

    @Override
    public String getBotPath() {
        return botConfiguration.getWebhookPath();
    }

    @PostConstruct
    private void setOwnWebhook() {
        System.out.println(restTemplate.getForEntity("https://api.telegram.org/bot" + botConfiguration.getBotToken() + "/deletewebhook", String.class));
        System.out.println(restTemplate.getForEntity("https://api.telegram.org/bot" + botConfiguration.getBotToken() + "/setwebhook?url=" + botConfiguration.getWebhookPath(), String.class));
    }
}
