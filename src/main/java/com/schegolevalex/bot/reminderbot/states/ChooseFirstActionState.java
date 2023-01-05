package com.schegolevalex.bot.reminderbot.states;

import com.schegolevalex.bot.reminderbot.Constant;
import com.schegolevalex.bot.reminderbot.KeyboardFactory;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import java.util.Map;

@Component
public class ChooseFirstActionState extends UserState {

    public ChooseFirstActionState(TelegramWebhookBot bot) {
        super(bot);
    }

    @SneakyThrows
    @Override
    public void sendReply(Long chatId, Map<String, Integer> messageIds) {
        BotApiMethod<?> method;
        if (messageIds.get(String.valueOf(chatId)) == null) {
            method = new SendMessage();
            ((SendMessage) method).setChatId(String.valueOf(chatId));
            ((SendMessage) method).setText(Constant.CHOOSE_FIRST_ACTION_DESCRIPTION);
            ((SendMessage) method).setReplyMarkup(KeyboardFactory.withFirstActionMessage());
        } else {
            method = new EditMessageText();
            ((EditMessageText) method).setChatId(String.valueOf(chatId));
            ((EditMessageText) method).setText(Constant.CHOOSE_FIRST_ACTION_DESCRIPTION);
            ((EditMessageText) method).setReplyMarkup(KeyboardFactory.withFirstActionMessage());
        }
        bot.execute(method);
    }
}