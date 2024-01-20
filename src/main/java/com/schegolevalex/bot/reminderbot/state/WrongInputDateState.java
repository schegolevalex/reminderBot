package com.schegolevalex.bot.reminderbot.state;

import com.schegolevalex.bot.reminderbot.CustomReply;
import com.schegolevalex.bot.reminderbot.KeyboardFactory;
import com.schegolevalex.bot.reminderbot.ReminderBot;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.util.AbilityUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.schegolevalex.bot.reminderbot.Constant.Callback;
import static com.schegolevalex.bot.reminderbot.Constant.Message;

@Component
public class WrongInputDateState extends AbstractState {

    public WrongInputDateState(@Lazy ReminderBot bot) {
        super(bot);
    }

    @Override
    public CustomReply reply(Update update) {
        return CustomReply.builder()
                .text(Message.WRONG_DATE_FORMAT)
                .replyMarkup(KeyboardFactory.withBackButton())
                .build();
    }

    @Override
    public void perform(Update update) {
        Long chatId = AbilityUtils.getChatId(update);

        if (update.hasCallbackQuery() && update.getCallbackQuery().getData().equals(Callback.GO_BACK))
            bot.popBotState(chatId);
        else
            bot.pushBotState(chatId, State.WRONG_INPUT);
    }

    @Override
    public State getType() {
        return State.WRONG_INPUT_DATE;
    }
}
