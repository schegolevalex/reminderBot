package com.schegolevalex.bot.reminderbot.states;

import com.schegolevalex.bot.reminderbot.Constant;
import com.schegolevalex.bot.reminderbot.handlers.HandlerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public class WrongInputState extends UserState {

    private ReminderFacade reminderFacade;

    @Autowired
    public WrongInputState(@Lazy HandlerFactory handlerFactory,
                           @Lazy ReminderFacade reminderFacade) {
        super(handlerFactory);
        this.reminderFacade = reminderFacade;
    }

    @Override
    public SendMessage setText(SendMessage sendMessage) {
        Long chatId = Long.valueOf(sendMessage.getChatId());
        reminderFacade.getCurrentState(chatId).pop();
        String previousState = reminderFacade.getCurrentState(chatId).peek().getClass().getSimpleName();

        switch (previousState) {
            case ("AddingReminderDateState"):
                sendMessage.setText(Constant.WRONG_DATE_FORMAT);
                break;
            case ("AddingReminderTimeState"):
                sendMessage.setText(Constant.WRONG_TIME_FORMAT);
                break;
            default:
                sendMessage.setText(Constant.UNKNOWN_REQUEST);
                break;
        }
        return sendMessage;
    }
}
