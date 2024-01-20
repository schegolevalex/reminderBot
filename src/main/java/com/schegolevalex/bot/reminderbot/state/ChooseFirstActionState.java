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
public class ChooseFirstActionState extends AbstractState {

    public ChooseFirstActionState(@Lazy ReminderBot bot) {
        super(bot);
    }

    @Override
    public CustomReply reply(Update update) {
        return CustomReply.builder()
                .text(Message.CHOOSE_FIRST_ACTION_DESCRIPTION)
                .replyMarkup(KeyboardFactory.withFirstActionMessage())
                .build();
    }

    @Override
    public void perform(Update update) {
        Long chatId = AbilityUtils.getChatId(update);

        if (update.hasCallbackQuery()) {
            String data = update.getCallbackQuery().getData();
            if (data.equals(Callback.GO_TO_MY_REMINDERS)) {
                bot.pushBotState(chatId, State.WATCH_REMINDERS);
            } else if (data.equals(Callback.GO_TO_ADD_REMINDER)) {
                bot.pushBotState(chatId, State.ADD_REMINDER_TEXT);
            }
        } else
            bot.pushBotState(chatId, State.WRONG_INPUT);
    }

    @Override
    public State getType() {
        return State.CHOOSE_FIRST_ACTION;
    }
}