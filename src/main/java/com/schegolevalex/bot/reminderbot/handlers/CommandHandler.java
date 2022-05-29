package com.schegolevalex.bot.reminderbot.handlers;

import com.schegolevalex.bot.reminderbot.Constant;
import com.schegolevalex.bot.reminderbot.KeyboardFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class CommandHandler implements Handler {
    private static CommandHandler instance;

    private CommandHandler() {
    }

    @Override
    public BotApiMethod<?> handle(Update update, UserState state) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(update.getMessage().getChatId()));

//        switch (update.getMessage().getEntities().stream()
//                .map(entity -> entity.getText())
//                .filter(entity -> entity.startsWith("/"))
//                .map(entity -> entity.substring(1))
//                .findFirst().orElse("")) {
        switch (update.getMessage().getText()) {
            case ("/start"):
                sendMessage.setText(Constant.START_DESCRIPTION);
                sendMessage.setReplyMarkup(KeyboardFactory.withStartMessage());
                state = UserState.CHOOSING_FIRST_ACTION;
                break;

            default:
                sendMessage.setText(Constant.UNKNOWN_REQUEST);
                break;
        }

        return sendMessage;
    }

    public static CommandHandler getInstance() {
        if (instance == null) {
            instance = new CommandHandler();
        }
        return instance;
    }
}
