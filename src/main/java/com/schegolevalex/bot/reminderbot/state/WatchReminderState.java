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

import java.util.Optional;
import java.util.UUID;

@Component
public class WatchReminderState extends AbstractState {
    private final ReminderService reminderService;

    public WatchReminderState(@Lazy ReminderBot bot,
                              ReminderService reminderService) {
        super(bot);
        this.reminderService = reminderService;
    }

    @Override
    public BotApiMethod<?> reply(Update update) {
        Long chatId = AbilityUtils.getChatId(update);

        if (update.hasCallbackQuery()) {
            String data = update.getCallbackQuery().getData();
            String id = data.substring(data.length() - 36);
            Optional<Reminder> mayBeReminder = reminderService.getReminderById(UUID.fromString(id));
            if (mayBeReminder.isPresent()) {
                Reminder reminder = mayBeReminder.get();
                return SendMessage.builder()
                        .chatId(chatId)
                        .text(reminder.toUserView())
                        .replyMarkup(KeyboardFactory.withReminderMessage())
                        .build();
            }
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

        if (update.hasCallbackQuery()) {
            switch (update.getCallbackQuery().getData()) {
                case (Constant.Callback.GO_TO_EDIT_REMINDER) -> bot.pushBotState(chatId, State.EDIT_REMINDER);
                case (Constant.Callback.GO_TO_DELETE_REMINDER) -> bot.pushBotState(chatId, State.DELETE_REMINDER);
                case (Constant.Callback.GO_BACK) -> bot.popBotState(chatId);
            }
        } else
            bot.pushBotState(chatId, State.WRONG_INPUT);
    }

    @Override
    public State getType() {
        return State.WATCH_REMINDER;
    }
}
