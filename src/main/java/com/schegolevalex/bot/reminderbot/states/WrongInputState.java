package com.schegolevalex.bot.reminderbot.states;

import com.schegolevalex.bot.reminderbot.Constant;
import com.schegolevalex.bot.reminderbot.handlers.HandlerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public class WrongInputState extends UserState {

    @Autowired
    public WrongInputState(@Lazy ReminderFacade reminderFacade,
                           @Lazy HandlerFactory handlerFactory) {
        super(reminderFacade, handlerFactory);
    }

    @Override
    public SendMessage setText(SendMessage sendMessage) {
        Long chatId = Long.valueOf(sendMessage.getChatId());
        reminderFacade.getCurrentState(chatId).pop();
        Class<? extends UserState> aClass = reminderFacade.getCurrentState(chatId).peek().getClass();
        if (AddingReminderDateState.class.equals(aClass)) {
            sendMessage.setText(Constant.WRONG_DATE_FORMAT);
        } else if (AddingReminderTimeState.class.equals(aClass)) {
            sendMessage.setText(Constant.WRONG_TIME_FORMAT);
        } else {
            sendMessage.setText(Constant.UNKNOWN_REQUEST);
        }
        return sendMessage;
    }
}
