package com.schegolevalex.bot.reminderbot.repliers;

import com.schegolevalex.bot.reminderbot.Constant;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Map;

@Component
public class AwaitStartReplier extends AbstractReplier {

    public AwaitStartReplier(TelegramWebhookBot bot) {
        super(bot);
    }

    @SneakyThrows
    @Override
    public void reply(Long chatId, Map<String, Integer> messageIds) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(Constant.AWAIT_START_DESCRIPTION);
        messageIds.put(String.valueOf(chatId), bot.execute(sendMessage).getMessageId());
    }
}
