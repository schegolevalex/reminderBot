package com.schegolevalex.bot.reminderbot.states;

import com.schegolevalex.bot.reminderbot.Constant;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import java.util.Map;

@Component
public class WrongInputTimeState extends UserState {

    public WrongInputTimeState(TelegramWebhookBot bot) {
        super(bot);
    }

    @SneakyThrows
    @Override
    public void sendReply(Long chatId, Map<String, Integer> messageIds) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(String.valueOf(chatId));
        editMessageText.setText(Constant.WRONG_TIME_FORMAT);
        bot.execute(editMessageText);
    }
}
