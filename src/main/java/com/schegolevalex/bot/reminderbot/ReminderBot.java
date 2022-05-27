package com.schegolevalex.bot.reminderbot;

import com.schegolevalex.bot.reminderbot.config.BotConfiguration;
import com.schegolevalex.bot.reminderbot.handlers.ResponseHandler;
import com.schegolevalex.bot.reminderbot.handlers.UserState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
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
import java.util.Map;

@Component
public class ReminderBot extends TelegramWebhookBot {

    private final BotConfiguration botConfiguration;
//    private final RestTemplate restTemplate;
    private final ResponseHandler responseHandler;

    private Map<Long, UserState> statesDB;

    @Autowired
    public ReminderBot(BotConfiguration botConfiguration, /*RestTemplate restTemplate,*/
                       ResponseHandler responseHandler, Map <Long, UserState> statesDB) {
        this.botConfiguration = botConfiguration;
//        this.restTemplate = restTemplate;
        this.responseHandler = responseHandler;
        this.statesDB = statesDB;
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

//        restTemplate.getForEntity("https://api.telegram.org/bot" + botConfiguration.getBotToken() +
//                "/" + DeleteWebhook.PATH, String.class);
//        restTemplate.getForEntity("https://api.telegram.org/bot" + botConfiguration.getBotToken()
//                + "/" + SetWebhook.PATH + "?url=" + botConfiguration.getWebhookPath(), String.class);
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
