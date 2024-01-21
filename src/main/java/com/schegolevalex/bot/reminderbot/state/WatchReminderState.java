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

import java.util.Optional;
import java.util.UUID;

import static com.schegolevalex.bot.reminderbot.config.Constant.Callback;
import static com.schegolevalex.bot.reminderbot.config.Constant.Message;

@Component
public class WatchReminderState extends AbstractState {
    private final ReminderService reminderService;

    public WatchReminderState(@Lazy ReminderBot bot,
                              ReminderService reminderService) {
        super(bot);
        this.reminderService = reminderService;
    }

    @Override
    public CustomReply reply(Update update) {
        String data = update.getCallbackQuery().getData();
        String id = data.substring(data.length() - 36);
        Optional<Reminder> mayBeReminder = reminderService.getReminderById(UUID.fromString(id));

        if (mayBeReminder.isPresent()) {
            Reminder reminder = mayBeReminder.get();
            return CustomReply.builder()
                    .text(reminder.toUserView())
                    .replyMarkup(KeyboardFactory.withReminderMessage(reminder))
                    .build();
        } else
            return CustomReply.builder()
                    .text(Message.UNKNOWN_REMINDER)
                    .replyMarkup(KeyboardFactory.withBackButton())
                    .build();
    }

    @Override
    public void perform(Update update) {
        Long chatId = AbilityUtils.getChatId(update);

        if (update.hasCallbackQuery()) {
            String data = update.getCallbackQuery().getData();
            if (data.startsWith(Callback.GO_TO_EDIT_REMINDER_TEXT))
                bot.pushState(chatId, State.EDIT_REMINDER_TEXT);
            if (data.startsWith(Callback.GO_TO_EDIT_REMINDER_DATE))
                bot.pushState(chatId, State.EDIT_REMINDER_DATE);
            if (data.startsWith(Callback.GO_TO_EDIT_REMINDER_TIME))
                bot.pushState(chatId, State.EDIT_REMINDER_TIME);
            if (data.startsWith(Callback.GO_TO_CONFIRM_TO_DELETE_REMINDER))
                bot.pushState(chatId, State.CONFIRM_DELETE_REMINDER);
            if (data.equals(Callback.GO_BACK))
                bot.popState(chatId);
        } else
            bot.pushState(chatId, State.WRONG_INPUT);
    }

    @Override
    public State getType() {
        return State.WATCH_REMINDER;
    }
}
