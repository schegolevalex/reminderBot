package com.schegolevalex.bot.reminderbot.state;

import com.schegolevalex.bot.reminderbot.Constant;
import com.schegolevalex.bot.reminderbot.ReminderBot;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.util.AbilityUtils;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class AwaitStartState extends AbstractState {

    public AwaitStartState(@Lazy ReminderBot bot) {
        super(bot);
    }

    @Override
    public BotApiMethod<?> reply(Update update) {
        return SendMessage.builder()
                .chatId(AbilityUtils.getChatId(update))
                .text(Constant.AWAIT_START_DESCRIPTION)
                .build();
    }

    @Override
    public void perform(Update update) {
        Long chatId = AbilityUtils.getChatId(update);

        if (update.hasMessage() && update.getMessage().hasText())
            switch (update.getMessage().getText()) {
                case "/start" -> bot.pushBotState(chatId, State.CHOOSE_FIRST_ACTION);
            }
        else
            bot.pushBotState(chatId, State.WRONG_INPUT);
    }

    @Override
    public State getType() {
        return State.AWAIT_START;
    }
}
