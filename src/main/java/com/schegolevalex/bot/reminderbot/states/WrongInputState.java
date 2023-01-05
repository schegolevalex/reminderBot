package com.schegolevalex.bot.reminderbot.states;

import com.schegolevalex.bot.reminderbot.Constant;
import com.schegolevalex.bot.reminderbot.ReminderFacade;
import com.schegolevalex.bot.reminderbot.handlers.HandlerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

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
    public BotApiMethod getReply(Long chatId) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(String.valueOf(chatId));

        reminderFacade.getCurrentState(chatId).pop();
        String previousState = reminderFacade.getCurrentState(chatId).peek().getClass().getSimpleName();

        switch (previousState) {
            case ("AddingReminderDateState"):
                editMessageText.setText(Constant.WRONG_DATE_FORMAT);
                break;
            case ("AddingReminderTimeState"):
                editMessageText.setText(Constant.WRONG_TIME_FORMAT);
                break;
            default:
                editMessageText.setText(Constant.UNKNOWN_REQUEST);
                break;
        }
        return editMessageText;
    }
}
