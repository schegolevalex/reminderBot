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
public class WrongInputState extends AbstractState {

    public WrongInputState(@Lazy ReminderBot bot) {
        super(bot);
    }

    @Override
    public BotApiMethod<?> reply(Update update) {
        return SendMessage.builder()
                .chatId(AbilityUtils.getChatId(update))
//                .messageId(update.getMessage().getMessageId())
                .text(Constant.UNKNOWN_REQUEST)
                .replyMarkup(KeyboardFactory.withFirstActionMessage())
                .build();
    }

    @Override
    public void perform(Update update) {
        Long chatId = AbilityUtils.getChatId(update);

        if (update.hasCallbackQuery())
            switch (update.getCallbackQuery().getData()) {
                case (Constant.Callback.GO_TO_MY_REMINDERS) -> bot.pushBotState(chatId, State.WATCH_REMINDERS);
                case (Constant.Callback.GO_TO_ADD_REMINDER) -> bot.pushBotState(chatId, State.ADD_REMINDER_TEXT);
            }
        else
            bot.pushBotState(chatId, State.WRONG_INPUT);
    }

    @Override
    public State getType() {
        return State.WRONG_INPUT;
    }
}
