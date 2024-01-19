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

import java.util.List;

@Component
public class WatchRemindersState extends AbstractState {
    private final ReminderService reminderService;

    public WatchRemindersState(@Lazy ReminderBot bot,
                               ReminderService reminderService) {
        super(bot);
        this.reminderService = reminderService;
    }

    @Override
    public BotApiMethod<?> reply(Update update) {
        Long chatId = AbilityUtils.getChatId(update);
        List<Reminder> reminders = reminderService.getAllRemindersByChatId(chatId);

        if (reminders.isEmpty())
            return SendMessage.builder()
                    .chatId(chatId)
                    .text(Constant.MY_REMINDERS + Constant.REMINDER_LIST_IS_EMPTY)
                    .replyMarkup(KeyboardFactory.withBackButton())
                    .build();
        else
            return SendMessage.builder()
                    .chatId(chatId)
                    .text(Constant.MY_REMINDERS)
                    .replyMarkup(KeyboardFactory.withRemindersMessage(reminders))
                    .build();
    }

    @Override
    public void perform(Update update) {
        Long chatId = AbilityUtils.getChatId(update);

        if (update.hasCallbackQuery()) {
            if (update.getCallbackQuery().getData().startsWith(Constant.Callback.GO_TO_MY_REMINDER))
                bot.pushBotState(chatId, State.WATCH_REMINDER);
            if (update.getCallbackQuery().getData().equals(Constant.Callback.GO_BACK))
                bot.popBotState(chatId);
        } else
            bot.pushBotState(chatId, State.WRONG_INPUT);
    }

    @Override
    public State getType() {
        return State.WATCH_REMINDERS;
    }
}
