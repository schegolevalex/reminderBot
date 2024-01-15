package com.schegolevalex.bot.reminderbot.state;

import com.schegolevalex.bot.reminderbot.ReminderBot;
import org.springframework.context.annotation.Lazy;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

//@Getter
public abstract class AbstractState {

    final ReminderBot bot;

    public AbstractState(@Lazy ReminderBot bot) {
        this.bot = bot;
    }

    public abstract BotApiMethod<?> reply(Update update);

    public abstract void perform(Update update);

    public abstract State getType();
}