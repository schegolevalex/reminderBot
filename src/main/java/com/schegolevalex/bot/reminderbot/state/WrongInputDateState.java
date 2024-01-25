package com.schegolevalex.bot.reminderbot.state;

import com.schegolevalex.bot.reminderbot.CustomReply;
import com.schegolevalex.bot.reminderbot.KeyboardFactory;
import com.schegolevalex.bot.reminderbot.ReminderBot;
import com.schegolevalex.bot.reminderbot.entity.Reminder;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.util.AbilityUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static com.schegolevalex.bot.reminderbot.config.Constant.Callback;
import static com.schegolevalex.bot.reminderbot.config.Constant.Message;

@Component
public class WrongInputDateState extends AbstractState {

    public WrongInputDateState(@Lazy ReminderBot bot) {
        super(bot);
    }

    @Override
    public CustomReply reply(Update update) {
        return CustomReply.builder()
                .text(Message.WRONG_DATE_FORMAT)
                .replyMarkup(KeyboardFactory.withMainPageButton())
                .build();
    }

    @Override
    public void perform(Update update) {
        Long chatId = AbilityUtils.getChatId(update);

        if (update.hasMessage() && update.getMessage().hasText()) {
            Reminder tempReminder = bot.getChatContext(chatId).getTempReminder();
            String text = update.getMessage().getText();
            LocalDate date;
            try {
                date = LocalDate.parse(text, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            } catch (DateTimeParseException e) {
                bot.pushState(chatId, State.WRONG_INPUT_DATE);
                return;
            }
            tempReminder.setDate(date);
            bot.pushState(chatId, State.ADD_REMINDER_TIME);
        } else if (update.hasCallbackQuery() && update.getCallbackQuery().getData().equals(Callback.GO_TO_MAIN))
            bot.pushState(chatId, State.CHOOSE_FIRST_ACTION);
        else
            bot.pushState(chatId, State.WRONG_INPUT_DATE);
    }

    @Override
    public State getType() {
        return State.WRONG_INPUT_DATE;
    }
}
