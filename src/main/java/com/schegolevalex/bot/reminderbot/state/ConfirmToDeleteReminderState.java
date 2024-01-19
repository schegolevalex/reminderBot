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
public class ConfirmToDeleteReminderState extends AbstractState {
    private final ReminderService reminderService;

    public ConfirmToDeleteReminderState(@Lazy ReminderBot bot,
                                        ReminderService reminderService) {
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
            return SendMessage.builder()
                    .chatId(chatId)
                    .text(reminder.toUserView() + "\n" + Constant.DELETE_CONFIRMATION)
                    .replyMarkup(KeyboardFactory.withDeleteReminderConfirmation(reminder))
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

        if (update.hasCallbackQuery()) {
            if (update.getCallbackQuery().getData().startsWith(Constant.Callback.GO_TO_CONFIRMED_DELETION)) {
                String data = update.getCallbackQuery().getData();
                String id = data.substring(data.length() - 36);
                Optional<Reminder> mayBeReminder = reminderService.getReminderById(UUID.fromString(id));

                if (mayBeReminder.isPresent()) {
                    reminderService.deleteReminder(mayBeReminder.get().getId());
                    bot.pushBotState(chatId, State.SUCCESSFUL_DELETION);
                } else
                    bot.pushBotState(chatId, State.WRONG_INPUT);
            }

            if (update.getCallbackQuery().getData().equals(Constant.Callback.GO_BACK))
                bot.popBotState(chatId);
        } else
            bot.pushBotState(chatId, State.WRONG_INPUT);
    }

    @Override
    public State getType() {
        return State.CONFIRM_DELETE_REMINDER;
    }
}
