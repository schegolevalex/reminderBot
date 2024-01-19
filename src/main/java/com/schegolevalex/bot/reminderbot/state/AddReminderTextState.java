package com.schegolevalex.bot.reminderbot.state;

import com.schegolevalex.bot.reminderbot.Constant;
import com.schegolevalex.bot.reminderbot.KeyboardFactory;
import com.schegolevalex.bot.reminderbot.ReminderBot;
import com.schegolevalex.bot.reminderbot.entity.Reminder;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.util.AbilityUtils;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class AddReminderTextState extends AbstractState {

    public AddReminderTextState(@Lazy ReminderBot bot) {
        super(bot);
    }

    @Override
    public BotApiMethod<?> reply(Update update) {
        return SendMessage.builder()
                .chatId(AbilityUtils.getChatId(update))
//                .messageId(update.getMessage().getMessageId())
                .text(Constant.ADD_REMINDER_TEXT_DESCRIPTION)
                .replyMarkup(KeyboardFactory.withBackButton())
                .build();
    }

    @Override
    public void perform(Update update) {
        Long chatId = AbilityUtils.getChatId(update);

        if (update.hasCallbackQuery())
            switch (update.getCallbackQuery().getData()) {
                case (Constant.Callback.GO_BACK) -> bot.popBotState(chatId);
                // case2..caseN
            }
        else if (update.hasMessage() && update.getMessage().hasText()) {
            Reminder tempReminder = new Reminder();
            tempReminder.setChatId(chatId);
            tempReminder.setText(update.getMessage().getText());
            bot.getRemindersContext().put(chatId, tempReminder);
            bot.pushBotState(chatId, State.ADD_REMINDER_DATE);
        } else
            bot.pushBotState(chatId, State.WRONG_INPUT);
    }

    @Override
    public State getType() {
        return State.ADD_REMINDER_TEXT;
    }
}
