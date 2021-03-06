package com.schegolevalex.bot.reminderbot.states;

import com.schegolevalex.bot.reminderbot.Constant;
import com.schegolevalex.bot.reminderbot.KeyboardFactory;
import com.schegolevalex.bot.reminderbot.ReminderFacade;
import com.schegolevalex.bot.reminderbot.handlers.HandlerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

@Component
public class ChoosingFirstActionState extends UserState {
    private final ReminderFacade reminderFacade;

    @Autowired
    public ChoosingFirstActionState(@Lazy HandlerFactory handlerFactory,
                                    @Lazy ReminderFacade reminderFacade) {
        super(handlerFactory);
        this.reminderFacade = reminderFacade;
    }

    @Override
    public BotApiMethod getReply(Long chatId) {

        BotApiMethod method;
        if (reminderFacade.getMessageIds().get(String.valueOf(chatId)) == null) {
            method = new SendMessage();
            ((SendMessage) method).setChatId(String.valueOf(chatId));
            ((SendMessage) method).setText(Constant.CHOOSING_FIRST_ACTION_DESCRIPTION);
            ((SendMessage) method).setReplyMarkup(KeyboardFactory.withFirstActionMessage());
        } else {
            method = new EditMessageText();
            ((EditMessageText) method).setChatId(String.valueOf(chatId));
            ((EditMessageText) method).setText(Constant.CHOOSING_FIRST_ACTION_DESCRIPTION);
            ((EditMessageText) method).setReplyMarkup(KeyboardFactory.withFirstActionMessage());
        }
        return method;
    }
}