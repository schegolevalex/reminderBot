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

import static com.schegolevalex.bot.reminderbot.config.Constant.Callback;
import static com.schegolevalex.bot.reminderbot.config.Constant.Message;

@Component
public class AddReminderDateState extends AbstractState {

    public AddReminderDateState(@Lazy ReminderBot bot) {
        super(bot);
    }

    @Override
    public CustomReply reply(Update update) {
        Long chatId = AbilityUtils.getChatId(update);
        String reminderText = bot.getChatContext(chatId).getTempReminder().getText();
        LocalDate targetDate = LocalDate.now();

        if (update.hasCallbackQuery()) {
            String data = update.getCallbackQuery().getData();
            if (data.startsWith(Callback.CHANGE_MONTH))
                targetDate = LocalDate.parse(data.substring(data.length() - 10));
        }

        return CustomReply.builder()
                .text("Текст напоминания: \"" + reminderText + "\"\n" + Message.ADD_REMINDER_DATE_DESCRIPTION)
                .replyMarkup(KeyboardFactory.withCalendar(targetDate))
                .build();
    }

    @Override
    public void perform(Update update) {
        Long chatId = AbilityUtils.getChatId(update);

        if (update.hasCallbackQuery()) {
            String data = update.getCallbackQuery().getData();
            Reminder tempReminder = bot.getChatContext(chatId).getTempReminder();

            if (data.equals(Callback.GO_BACK))
                bot.popState(chatId);
            else if (data.startsWith(Callback.DATE)) {
                tempReminder.setDate(LocalDate.parse(data.substring(data.length() - 10)));
                bot.pushState(chatId, State.ADD_REMINDER_TIME);
            }
        } else
            bot.pushState(chatId, State.WRONG_INPUT);
    }

    @Override
    public State getType() {
        return State.ADD_REMINDER_DATE;
    }
}
