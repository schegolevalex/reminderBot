package com.schegolevalex.bot.reminderbot.states;

import com.schegolevalex.bot.reminderbot.Constant;
import com.schegolevalex.bot.reminderbot.ReminderFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

@Component
public class WrongInputState implements UserState {

    private final ReminderFacade reminderFacade;

    @Autowired
    public WrongInputState(@Lazy ReminderFacade reminderFacade) {
        this.reminderFacade = reminderFacade;
    }

    @Override
    public BotApiMethod<?> sendReply(Long chatId) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(String.valueOf(chatId));

        reminderFacade.getCurrentStateStack(chatId).pop();
        String previousState = reminderFacade.getCurrentStateStack(chatId).peek().getClass().getSimpleName();

        switch (previousState) {
            case ("AddingReminderDateState") -> editMessageText.setText(Constant.WRONG_DATE_FORMAT);
            case ("AddingReminderTimeState") -> editMessageText.setText(Constant.WRONG_TIME_FORMAT);
            default -> editMessageText.setText(Constant.UNKNOWN_REQUEST);
        }
        return editMessageText;
    }
}
