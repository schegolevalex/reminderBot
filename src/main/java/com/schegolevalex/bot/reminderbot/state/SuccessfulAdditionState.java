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

import java.time.format.DateTimeFormatter;

@Component
public class SuccessfulAdditionState extends AbstractState {

    public SuccessfulAdditionState(@Lazy ReminderBot bot) {
        super(bot);
    }

    @Override
    public BotApiMethod<?> reply(Update update) {
        Long chatId = AbilityUtils.getChatId(update);
        Reminder reminder = bot.getTempReminders().get(chatId);
        return SendMessage.builder()
                .chatId(chatId)
                .text(String.format(Constant.SUCCESSFUL_ADDITION,
                        reminder.getText(),
                        reminder.getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                        reminder.getTime().toString()))
                .replyMarkup(KeyboardFactory.withMainPageButton())
                .build();
    }

    @Override
    public void perform(Update update) {
        Long chatId = AbilityUtils.getChatId(update);

        if (update.hasCallbackQuery())
            bot.pushBotState(chatId, State.CHOOSE_FIRST_ACTION);
        else
            bot.pushBotState(chatId, State.WRONG_INPUT);
    }

    @Override
    public State getType() {
        return State.SUCCESSFUL_ADDITION;
    }
}
