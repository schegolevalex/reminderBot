package com.schegolevalex.bot.reminderbot.state;

import com.schegolevalex.bot.reminderbot.Constant;
import com.schegolevalex.bot.reminderbot.ReminderBot;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.util.AbilityUtils;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class WrongInputCommonState extends AbstractState {

    public WrongInputCommonState(@Lazy ReminderBot bot) {
        super(bot);
    }

    @Override
    public void handle(Update update) {

    }

    @Override
    public BotApiMethod<?> reply(Update update) {
        return EditMessageText.builder()
                .chatId(AbilityUtils.getChatId(update))
                .messageId(update.getMessage().getMessageId())
                .text(Constant.UNKNOWN_REQUEST)
                .build();
    }

    @Override
    public State getType() {
        return State.WRONG_INPUT_COMMON;
    }
}
