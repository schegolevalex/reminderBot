package com.schegolevalex.bot.reminderbot.state;

import com.schegolevalex.bot.reminderbot.CustomReply;
import com.schegolevalex.bot.reminderbot.KeyboardFactory;
import com.schegolevalex.bot.reminderbot.ReminderBot;
import com.schegolevalex.bot.reminderbot.entity.Reminder;
import com.schegolevalex.bot.reminderbot.service.ReminderService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.util.AbilityUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static com.schegolevalex.bot.reminderbot.Constant.Callback;
import static com.schegolevalex.bot.reminderbot.Constant.Message;

@Component
public class AddReminderTimeState extends AbstractState {
    private final ReminderService reminderService;

    public AddReminderTimeState(@Lazy ReminderBot bot,
                                ReminderService reminderService) {
        super(bot);
        this.reminderService = reminderService;
    }

    @Override
    public CustomReply reply(Update update) {
        Long chatId = AbilityUtils.getChatId(update);
        String reminderText = bot.getRemindersContext().get(chatId).getText();
        String date = bot.getRemindersContext().get(chatId).getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        return CustomReply.builder()
                .text("Текст напоминания: \"" + reminderText + "\"\n" +
                        "Дата напоминания: " + date + "\"\n" +
                        Message.ADD_REMINDER_TIME_DESCRIPTION)
                .replyMarkup(KeyboardFactory.withBackButton())
                .build();
    }

    @Override
    public void perform(Update update) {
        Long chatId = AbilityUtils.getChatId(update);

        if (update.hasMessage() && update.getMessage().hasText()) {
            Reminder tempReminder = bot.getRemindersContext().get(chatId);
            String text = update.getMessage().getText();
            LocalTime time;
            try {
                time = LocalTime.parse(text);
            } catch (DateTimeParseException e) {
                bot.pushBotState(chatId, State.WRONG_INPUT_TIME);
                return;
            }
            tempReminder.setTime(time);

            reminderService.saveReminder(tempReminder);
            bot.remind(tempReminder);
            bot.pushBotState(chatId, State.SUCCESSFUL_ADDITION);
        } else if (update.hasCallbackQuery() && update.getCallbackQuery().getData().equals(Callback.GO_BACK))
            bot.popBotState(chatId);
        else
            bot.pushBotState(chatId, State.WRONG_INPUT_DATE);
    }

    @Override
    public State getType() {
        return State.ADD_REMINDER_TIME;
    }
}
