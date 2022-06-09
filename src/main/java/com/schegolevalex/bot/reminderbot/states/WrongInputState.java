package com.schegolevalex.bot.reminderbot.states;

import com.schegolevalex.bot.reminderbot.Constant;
import com.schegolevalex.bot.reminderbot.handlers.HandlerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public class WrongInputState extends UserState {

    @Autowired
    public WrongInputState(ReminderFacade reminderFacade,
                           HandlerFactory handlerFactory) {
        super(reminderFacade, handlerFactory);
    }

    @Override
    public SendMessage setText(SendMessage sendMessage) {
        Long chatId = Long.valueOf(sendMessage.getChatId());
        reminderFacade.getCurrentState(chatId).pop();
        switch (reminderFacade.getCurrentState(chatId).peek()) {
            case
        }


        sendMessage.setText(Constant.UNKNOWN_REQUEST);
        return sendMessage;
    }
}
