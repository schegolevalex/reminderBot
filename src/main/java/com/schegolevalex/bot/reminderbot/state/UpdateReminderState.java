package com.schegolevalex.bot.reminderbot.state;

import com.schegolevalex.bot.reminderbot.ReminderBot;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class UpdateReminderState extends AbstractState {

    public UpdateReminderState(@Lazy ReminderBot bot) {
        super(bot);
    }

    @Override
    public void handle(Update update) {

    }

    @Override
    public BotApiMethod<?> reply(Update update) {
        return null;
    }
}
