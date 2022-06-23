package com.schegolevalex.bot.reminderbot.states;

import com.schegolevalex.bot.reminderbot.Constant;
import com.schegolevalex.bot.reminderbot.KeyboardFactory;
import com.schegolevalex.bot.reminderbot.handlers.HandlerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

@Component
public class SuccessfulAdditionState extends UserState {

    @Autowired
    public SuccessfulAdditionState(@Lazy HandlerFactory handlerFactory) {
        super(handlerFactory);
    }

    @Override
    public BotApiMethod getReply(Long chatId) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(String.valueOf(chatId));
        editMessageText.setText(Constant.SUCCESSFUL_ADDITION + " " + Constant.CHOOSING_FIRST_ACTION_DESCRIPTION);
        editMessageText.setReplyMarkup(KeyboardFactory.withFirstActionMessage());
        return editMessageText;
    }
}
