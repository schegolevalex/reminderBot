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

import java.util.List;
import java.util.UUID;

import static com.schegolevalex.bot.reminderbot.config.Constant.Callback;
import static com.schegolevalex.bot.reminderbot.config.Constant.Message;

@Component
public class WatchRemindersState extends AbstractState {
    private final ReminderService reminderService;

    public WatchRemindersState(@Lazy ReminderBot bot,
                               ReminderService reminderService) {
        super(bot);
        this.reminderService = reminderService;
    }

    @Override
    public CustomReply reply(Update update) {
        List<Reminder> reminders = reminderService.getAllRemindersByChatId(AbilityUtils.getChatId(update));

        if (reminders.isEmpty())
            return CustomReply.builder()
                    .text(Message.MY_REMINDERS + Message.REMINDER_LIST_IS_EMPTY)
                    .replyMarkup(KeyboardFactory.withBackButton())
                    .build();
        else
            return CustomReply.builder()
                    .text(Message.MY_REMINDERS)
                    .replyMarkup(KeyboardFactory.withRemindersMessage(reminders))
                    .build();
    }

    @Override
    public void perform(Update update) {
        Long chatId = AbilityUtils.getChatId(update);

        if (update.hasCallbackQuery()) {
            String data = update.getCallbackQuery().getData();
            if (data.startsWith(Callback.GO_TO_MY_REMINDER)) {
                String id = data.substring(data.length() - 36);
                reminderService.getReminderById(UUID.fromString(id))
                        .ifPresent(reminder -> bot.getChatContext(chatId).setTempReminder(reminder));
                bot.pushState(chatId, State.WATCH_REMINDER);
            }
            if (data.equals(Callback.GO_BACK))
                bot.popState(chatId);
        } else
            bot.pushState(chatId, State.WRONG_INPUT);
    }

    @Override
    public State getType() {
        return State.WATCH_REMINDERS;
    }
}
