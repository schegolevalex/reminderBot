package com.schegolevalex.bot.reminderbot.state;

import com.schegolevalex.bot.reminderbot.Constant;
import com.schegolevalex.bot.reminderbot.KeyboardFactory;
import com.schegolevalex.bot.reminderbot.ReminderBot;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.util.AbilityUtils;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class WrongInputDateState extends AbstractState {

    public WrongInputDateState(@Lazy ReminderBot bot) {
        super(bot);
    }

    @Override
    public BotApiMethod<?> reply(Update update) {
        return SendMessage.builder()
                .chatId(AbilityUtils.getChatId(update))
//                .messageId(update.getMessage().getMessageId())
                .text(Constant.WRONG_DATE_FORMAT)
                .replyMarkup(KeyboardFactory.withBackButton())
                .build();
    }

    @Override
    public void perform(Update update) {
        Long chatId = AbilityUtils.getChatId(update);

        if (update.hasCallbackQuery()) {
            switch (update.getCallbackQuery().getData()) {
                case Constant.Callback.GO_BACK -> bot.popBotState(chatId);
            }
        } else
            bot.pushBotState(chatId, State.WRONG_INPUT);
    }

    @Override
    public State getType() {
        return State.WRONG_INPUT_DATE;
    }
}
