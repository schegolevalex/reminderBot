package com.schegolevalex.bot.reminderbot.state;

import com.schegolevalex.bot.reminderbot.ReminderBot;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class WrongInputCommonState extends AbstractState {

    public WrongInputCommonState(@Lazy ReminderBot bot) {
        super(bot);
    }

    @Override
    public void handle(Update update) {

    }

    @Override
    public BotApiMethod<?> reply(Update update) {
        return null;
//        EditMessageText editMessageText = new EditMessageText();
//        editMessageText.setChatId(String.valueOf(chatId));
//        editMessageText.setText(Constant.UNKNOWN_REQUEST);
//        editMessageText.setMessageId(messageIds.get(String.valueOf(chatId)));
//        bot.execute(editMessageText);

    }
}
