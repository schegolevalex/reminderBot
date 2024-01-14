package com.schegolevalex.bot.reminderbot.state;

import com.schegolevalex.bot.reminderbot.ReminderBot;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Lazy;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Getter
@Setter
public abstract class AbstractState {

    private final ReminderBot bot;

    public AbstractState(@Lazy ReminderBot bot) {
        this.bot = bot;
    }

    public abstract void handle(Update update);

    public abstract BotApiMethod<?> reply(Update update);
}