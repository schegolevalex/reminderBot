package com.schegolevalex.bot.reminderbot.state;

import com.schegolevalex.bot.reminderbot.CustomReply;
import com.schegolevalex.bot.reminderbot.KeyboardFactory;
import com.schegolevalex.bot.reminderbot.ReminderBot;
import com.schegolevalex.bot.reminderbot.entity.Reminder;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.util.AbilityUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.schegolevalex.bot.reminderbot.config.Constant.Callback;
import static com.schegolevalex.bot.reminderbot.config.Constant.Message;

@Component
public class AddReminderTextState extends AbstractState {

    public AddReminderTextState(@Lazy ReminderBot bot) {
        super(bot);
    }

    @Override
    public CustomReply reply(Update update) {
        return CustomReply.builder()
                .text(Message.ADD_REMINDER_TEXT_DESCRIPTION)
                .replyMarkup(KeyboardFactory.withBackButton())
                .build();
    }

    @Override
    public void perform(Update update) {
        Long chatId = AbilityUtils.getChatId(update);

        if (update.hasMessage() && update.getMessage().hasText()) {
            Reminder tempReminder = new Reminder();
            tempReminder.setChatId(chatId);
            tempReminder.setText(update.getMessage().getText());
            bot.getRemindersContext().put(chatId, tempReminder);
            bot.pushState(chatId, State.ADD_REMINDER_DATE);
        } else if (update.hasCallbackQuery() && update.getCallbackQuery().getData().equals(Callback.GO_BACK))
            bot.popState(chatId);
        else
            bot.pushState(chatId, State.WRONG_INPUT);
    }

    @Override
    public State getType() {
        return State.ADD_REMINDER_TEXT;
    }
}
