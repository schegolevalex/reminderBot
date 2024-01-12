package com.schegolevalex.bot.reminderbot.repliers;

import com.schegolevalex.bot.reminderbot.Constant;
import com.schegolevalex.bot.reminderbot.KeyboardFactory;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import java.util.Map;

@Component
public class ChooseFirstActionReplier extends AbstractReplier {

    public ChooseFirstActionReplier(TelegramWebhookBot bot) {
        super(bot);
    }

    @SneakyThrows
    @Override
    public void reply(Long chatId, Map<String, Integer> messageIds) {
        if (messageIds.get(String.valueOf(chatId)) == null) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(chatId));
            sendMessage.setText(Constant.CHOOSE_FIRST_ACTION_DESCRIPTION);
            sendMessage.setReplyMarkup(KeyboardFactory.withFirstActionMessage());
            messageIds.put(String.valueOf(chatId), bot.execute(sendMessage).getMessageId());
        } else {
            EditMessageText editMessageText = new EditMessageText();
            editMessageText.setChatId(String.valueOf(chatId));
            editMessageText.setText(Constant.CHOOSE_FIRST_ACTION_DESCRIPTION);
            editMessageText.setReplyMarkup(KeyboardFactory.withFirstActionMessage());
            editMessageText.setMessageId(messageIds.get(String.valueOf(chatId)));
            bot.execute(editMessageText);
        }
    }
}