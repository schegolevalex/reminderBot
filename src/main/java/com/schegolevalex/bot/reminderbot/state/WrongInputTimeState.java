package com.schegolevalex.bot.reminderbot.state;

import com.schegolevalex.bot.reminderbot.Constant;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import java.util.Map;

@Component
public class WrongInputTimeState extends AbstractState {

    public WrongInputTimeState(TelegramWebhookBot bot) {
        super(bot);
    }

    @SneakyThrows
    @Override
    public void reply(Long chatId, Map<String, Integer> messageIds) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(String.valueOf(chatId));
        editMessageText.setText(Constant.WRONG_TIME_FORMAT);
        editMessageText.setMessageId(messageIds.get(String.valueOf(chatId)));
        bot.execute(editMessageText);
    }
}
