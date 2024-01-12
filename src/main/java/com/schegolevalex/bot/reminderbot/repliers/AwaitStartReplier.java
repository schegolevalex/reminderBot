package com.schegolevalex.bot.reminderbot.repliers;

import com.schegolevalex.bot.reminderbot.Constant;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public class AwaitStartReplier extends AbstractReplier {

//    public AwaitStartReplier(TelegramWebhookBot bot) {
//        super(bot);
//    }

    //    @SneakyThrows
    @Override
    public BotApiMethod<?> reply(Long chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(Constant.AWAIT_START_DESCRIPTION)
                .build();
//        messageIds.put(String.valueOf(chatId), bot.execute(sendMessage).getMessageId());
    }
}
