package com.schegolevalex.bot.reminderbot.state;

import com.schegolevalex.bot.reminderbot.Constant;
import com.schegolevalex.bot.reminderbot.KeyboardFactory;
import com.schegolevalex.bot.reminderbot.ReminderBot;
import com.schegolevalex.bot.reminderbot.entity.Reminder;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.util.AbilityUtils;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class AddReminderTextState extends AbstractState {

    public AddReminderTextState(@Lazy ReminderBot bot) {
        super(bot);
    }

    @Override
    public void handle(Update update) {
        Long chatId = AbilityUtils.getChatId(update);

        Reminder tempReminder = new Reminder();
        tempReminder.setChatId(chatId);
        tempReminder.setText(update.getMessage().getText());
        getBot().getTempReminders().put(chatId, tempReminder);
        getBot().pushBotState(chatId, getBot().findStateByType(State.ADD_REMINDER_DATE));
    }

    @Override
    public BotApiMethod<?> reply(Update update) {
        return EditMessageText.builder()
                .chatId(AbilityUtils.getChatId(update))
                .messageId(update.getMessage().getMessageId())
                .text(Constant.ADD_REMINDER_TEXT_DESCRIPTION)
                .replyMarkup(KeyboardFactory.withBackButton())
                .build();
    }

    @Override
    public State getType() {
        return State.ADD_REMINDER_TEXT;
    }
}
