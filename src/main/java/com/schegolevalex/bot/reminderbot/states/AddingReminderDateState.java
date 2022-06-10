package com.schegolevalex.bot.reminderbot.states;

import com.schegolevalex.bot.reminderbot.Constant;
import com.schegolevalex.bot.reminderbot.KeyboardFactory;
import com.schegolevalex.bot.reminderbot.handlers.HandlerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public class AddingReminderDateState extends UserState {

    public AddingReminderDateState(@Lazy ReminderFacade reminderFacade,
                                   @Lazy HandlerFactory handlerFactory) {
        super(reminderFacade, handlerFactory);
    }

    @Override
    public SendMessage setText(SendMessage sendMessage) {
        sendMessage.setText(Constant.ADDING_REMINDER_DATE_DESCRIPTION);
        sendMessage.setReplyMarkup(KeyboardFactory.withBackButton());
        return sendMessage;
    }
}
