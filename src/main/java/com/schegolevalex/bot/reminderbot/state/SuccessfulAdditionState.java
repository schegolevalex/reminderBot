package com.schegolevalex.bot.reminderbot.state;

import com.schegolevalex.bot.reminderbot.CustomReply;
import com.schegolevalex.bot.reminderbot.KeyboardFactory;
import com.schegolevalex.bot.reminderbot.ReminderBot;
import com.schegolevalex.bot.reminderbot.entity.Reminder;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.util.AbilityUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.format.DateTimeFormatter;

import static com.schegolevalex.bot.reminderbot.Constant.Message;

@Component
public class SuccessfulAdditionState extends AbstractState {

    public SuccessfulAdditionState(@Lazy ReminderBot bot) {
        super(bot);
    }

    @Override
    public CustomReply reply(Update update) {
        Reminder reminder = bot.getRemindersContext().get(AbilityUtils.getChatId(update));
        return CustomReply.builder()
                .text(String.format(Message.SUCCESSFUL_ADDITION,
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
