package com.schegolevalex.bot.reminderbot.state;

import com.schegolevalex.bot.reminderbot.CustomReply;
import com.schegolevalex.bot.reminderbot.ReminderBot;
import org.springframework.context.annotation.Lazy;
import org.telegram.telegrambots.meta.api.objects.Update;

//@Getter
public abstract class AbstractState {

    final ReminderBot bot;

    public AbstractState(@Lazy ReminderBot bot) {
        this.bot = bot;
    }

    public abstract CustomReply reply(Update update);

    public abstract void perform(Update update);

    public abstract State getType();
}