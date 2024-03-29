package com.schegolevalex.bot.reminderbot.state;

import com.schegolevalex.bot.reminderbot.CustomReply;
import com.schegolevalex.bot.reminderbot.KeyboardFactory;
import com.schegolevalex.bot.reminderbot.ReminderBot;
import com.schegolevalex.bot.reminderbot.config.Constant;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.util.AbilityUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.schegolevalex.bot.reminderbot.config.Constant.Message;

@Component
public class SuccessfulEditingState extends AbstractState {

    public SuccessfulEditingState(@Lazy ReminderBot bot) {
        super(bot);
    }

    @Override
    public CustomReply reply(Update update) {
        return CustomReply.builder()
                .text(Message.SUCCESSFUL_EDITING + "\n" + bot.getChatContext(AbilityUtils.getChatId(update)).getTempReminder().toUserView())
                .replyMarkup(KeyboardFactory.withOkButton())
                .build();
    }

    @Override
    public void perform(Update update) {
        Long chatId = AbilityUtils.getChatId(update);

        if (update.hasCallbackQuery()) {
            String data = update.getCallbackQuery().getData();
            if (data.equals(Constant.Callback.OK))
                bot.pushState(chatId, State.WATCH_REMINDERS);
            else if (data.equals(Constant.Callback.GO_BACK))
                bot.popState(chatId);
        } else
            bot.pushState(chatId, State.WRONG_INPUT);
    }

    @Override
    public State getType() {
        return State.SUCCESSFUL_EDITING;
    }
}
