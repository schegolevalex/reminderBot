package com.schegolevalex.bot.reminderbot.states;

import com.schegolevalex.bot.reminderbot.Constant;
import com.schegolevalex.bot.reminderbot.handlers.HandlerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Map;

@Component
public class WrongInputState extends UserState {

    private ReminderFacade reminderFacade;
    private final Map<String, UserState> statesMap;

    @Autowired
    public WrongInputState(@Lazy HandlerFactory handlerFactory,
                           @Lazy ReminderFacade reminderFacade,
                           Map<String, UserState> statesMap) {
        super(handlerFactory);
        this.reminderFacade = reminderFacade;
        this.statesMap = statesMap;
    }

    @Override
    public SendMessage setText(SendMessage sendMessage) {
        Long chatId = Long.valueOf(sendMessage.getChatId());
        reminderFacade.getCurrentState(chatId).pop();
        String name = reminderFacade.getCurrentState(chatId).peek().getClass().getSimpleName();

        switch (name) {
            case ("AddingReminderDateState"):
                sendMessage.setText(Constant.WRONG_DATE_FORMAT);
            case ("AddingReminderTimeState"):
                sendMessage.setText(Constant.WRONG_TIME_FORMAT);
            default:
                sendMessage.setText(Constant.UNKNOWN_REQUEST);
        }
        return sendMessage;
    }
}
