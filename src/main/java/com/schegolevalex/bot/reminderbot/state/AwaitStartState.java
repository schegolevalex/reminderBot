package com.schegolevalex.bot.reminderbot.state;

import com.schegolevalex.bot.reminderbot.CustomReply;
import com.schegolevalex.bot.reminderbot.ReminderBot;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.util.AbilityUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.schegolevalex.bot.reminderbot.Constant.Message;

@Component
public class AwaitStartState extends AbstractState {

    public AwaitStartState(@Lazy ReminderBot bot) {
        super(bot);
    }

    @Override
    public CustomReply reply(Update update) {
        return CustomReply.builder()
                .text(Message.AWAIT_START_DESCRIPTION)
                .build();
    }

    @Override
    public void perform(Update update) {
        Long chatId = AbilityUtils.getChatId(update);

        if (update.hasMessage() && update.getMessage().hasText()) {
            if (update.getMessage().getText().equals("/start"))
                bot.pushBotState(chatId, State.CHOOSE_FIRST_ACTION);
        } else
            bot.pushBotState(chatId, State.WRONG_INPUT);
    }

    @Override
    public State getType() {
        return State.AWAIT_START;
    }
}
