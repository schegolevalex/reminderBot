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
    public void handle(Update update) {
        Long chatId = AbilityUtils.getChatId(update);

        try {
            switch (update.getMessage().getText()) {
                case ("/start") -> getBot().pushBotState(chatId, getBot().findStateByType(State.CHOOSE_FIRST_ACTION));
                // todo
                default -> getBot().pushBotState(chatId, getBot().findStateByType(State.WRONG_INPUT_COMMON));
            }
        } catch (Exception e) {
            getBot().pushBotState(chatId, getBot().findStateByType(State.WRONG_INPUT_COMMON));
        }
    }

    @Override
    public BotApiMethod<?> reply(Update update) {
        return SendMessage.builder()
                .chatId(AbilityUtils.getChatId(update))
                .text(Constant.AWAIT_START_DESCRIPTION)
                .build();
    }

    @Override
    public State getType() {
        return State.AWAIT_START;
    }
}
