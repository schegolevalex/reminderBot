package com.schegolevalex.bot.reminderbot.states;

import com.schegolevalex.bot.reminderbot.Constant;
import com.schegolevalex.bot.reminderbot.KeyboardFactory;
import com.schegolevalex.bot.reminderbot.handlers.HandlerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public class AddingReminderTimeState extends UserState {

    public AddingReminderTimeState(@Lazy ReminderFacade reminderFacade,
                                   @Lazy HandlerFactory handlerFactory) {
        super(reminderFacade, handlerFactory);
    }

    @Override
    public SendMessage setText(SendMessage sendMessage) {
        sendMessage.setText(Constant.ADDING_REMINDER_TIME_DESCRIPTION);
        sendMessage.setReplyMarkup(KeyboardFactory.withBackButton());
        return sendMessage;
    }
}
