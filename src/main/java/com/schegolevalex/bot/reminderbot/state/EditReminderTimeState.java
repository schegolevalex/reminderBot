package com.schegolevalex.bot.reminderbot.state;

import com.schegolevalex.bot.reminderbot.Constant;
import com.schegolevalex.bot.reminderbot.KeyboardFactory;
import com.schegolevalex.bot.reminderbot.ReminderBot;
import com.schegolevalex.bot.reminderbot.entity.Reminder;
import com.schegolevalex.bot.reminderbot.services.ReminderService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.util.AbilityUtils;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.UUID;

@Component
public class EditReminderTimeState extends AbstractState {
    private final ReminderService reminderService;

    public EditReminderTimeState(@Lazy ReminderBot bot, ReminderService reminderService) {
        super(bot);
        this.reminderService = reminderService;
    }

    @Override
    public BotApiMethod<?> reply(Update update) {
        Long chatId = AbilityUtils.getChatId(update);

        String data = update.getCallbackQuery().getData();
        String id = data.substring(data.length() - 36);
        Optional<Reminder> mayBeReminder = reminderService.getReminderById(UUID.fromString(id));

        if (mayBeReminder.isPresent()) {
            Reminder reminder = mayBeReminder.get();
            bot.getRemindersContext().put(chatId, reminder);
            return SendMessage.builder()
                    .chatId(chatId)
                    .text(String.format(Constant.EDIT_REMINDER_TIME_DESCRIPTION, reminder.getTime()))
                    .replyMarkup(KeyboardFactory.withBackButton())
                    .build();
        }

        return SendMessage.builder()
                .chatId(chatId)
                .text(Constant.UNKNOWN_REMINDER)
                .replyMarkup(KeyboardFactory.withBackButton())
                .build();
    }

    @Override
    public void perform(Update update) {
        Long chatId = AbilityUtils.getChatId(update);
        if (update.hasMessage() && update.getMessage().hasText()) {
            LocalTime newTime;
            try {
                newTime = LocalTime.parse(update.getMessage().getText());
            } catch (DateTimeParseException e) {
                bot.pushBotState(chatId, State.WRONG_INPUT_TIME);
                return;
            }
            Reminder editedReminder = bot.getRemindersContext().get(chatId);
            editedReminder.setTime(newTime);
            reminderService.saveReminder(editedReminder);
            bot.pushBotState(chatId, State.SUCCESSFUL_EDITING);
        } else
            bot.pushBotState(chatId, State.WRONG_INPUT);
    }

    @Override
    public State getType() {
        return State.EDIT_REMINDER_TIME;
    }
}
