package com.schegolevalex.bot.reminderbot;

import com.schegolevalex.bot.reminderbot.config.BotConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class ReminderBot extends TelegramLongPollingBot {
    @Autowired
    BotConfiguration botConfiguration;

    @Override
    public String getBotUsername() {
        return botConfiguration.getName();
    }

    @Override
    public String getBotToken() {
        return botConfiguration.getBotToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        String chatId = String.valueOf(update.getMessage().getChatId());
        String message = update.getMessage().getText();

        SendMessage sendMessage = new SendMessage(chatId, message);
    }
}
