package com.schegolevalex.bot.reminderbot.state;

import com.schegolevalex.bot.reminderbot.CustomReply;
import com.schegolevalex.bot.reminderbot.KeyboardFactory;
import com.schegolevalex.bot.reminderbot.ReminderBot;
import com.schegolevalex.bot.reminderbot.config.Constant;
import com.schegolevalex.bot.reminderbot.entity.Reminder;
import com.schegolevalex.bot.reminderbot.service.ReminderService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.util.AbilityUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.schegolevalex.bot.reminderbot.config.Constant.Message;

@Component
public class EditReminderDateState extends AbstractState {
    private final ReminderService reminderService;

    public EditReminderDateState(@Lazy ReminderBot bot, ReminderService reminderService) {
        super(bot);
        this.reminderService = reminderService;
    }

    @Override
    public CustomReply reply(Update update) {
        Long chatId = AbilityUtils.getChatId(update);
        Reminder editedReminder = bot.getChatContext(chatId).getTempReminder();
        LocalDate targetDate = LocalDate.now();

        if (update.hasCallbackQuery()) {
            String data = update.getCallbackQuery().getData();
            if (data.startsWith(Constant.Callback.CHANGE_MONTH))
                targetDate = LocalDate.parse(data.substring(data.length() - 10));
        }

        return CustomReply.builder()
                .text(String.format(Message.EDIT_REMINDER_DATE_DESCRIPTION, editedReminder.getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))))
                .replyMarkup(KeyboardFactory.withCalendar(targetDate))
                .build();
    }

    @Override
    public void perform(Update update) {
        Long chatId = AbilityUtils.getChatId(update);

        if (update.hasCallbackQuery()) {
            String data = update.getCallbackQuery().getData();

            if (data.equals(Constant.Callback.GO_BACK))
                bot.popState(chatId);
            else {
                Reminder editedReminder = bot.getChatContext(chatId).getTempReminder();

                if (data.startsWith(Constant.Callback.DATE)) {
                    editedReminder.setDate(LocalDate.parse(data.substring(data.length() - 10)));
                    reminderService.saveReminder(editedReminder);
                    bot.pushState(chatId, State.SUCCESSFUL_EDITING);
                    bot.remind(editedReminder);
                }
            }
        } else
            bot.pushState(chatId, State.WRONG_INPUT);
    }

    @Override
    public State getType() {
        return State.EDIT_REMINDER_DATE;
    }
}
