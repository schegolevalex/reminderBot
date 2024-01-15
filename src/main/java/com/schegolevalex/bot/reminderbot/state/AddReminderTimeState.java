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
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
public class AddReminderTimeState extends AbstractState {
    private final ReminderService reminderService;

    public AddReminderTimeState(@Lazy ReminderBot bot,
                                ReminderService reminderService) {
        super(bot);
        this.reminderService = reminderService;
    }

    @Override
    public BotApiMethod<?> reply(Update update) {
        Long chatId = AbilityUtils.getChatId(update);
        String reminderText = bot.getTempReminders().get(chatId).getText();
        String date = bot.getTempReminders().get(chatId).getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        return SendMessage.builder()
                .chatId(chatId)
//                .messageId(update.getMessage().getMessageId())
                .text("Текст напоминания: \"" + reminderText + "\"\n" +
                        "Дата напоминания: " + date + "\"\n" +
                        Constant.ADD_REMINDER_TIME_DESCRIPTION)
                .replyMarkup(KeyboardFactory.withBackButton())
                .build();
    }

    @Override
    public void perform(Update update) {
        Long chatId = AbilityUtils.getChatId(update);

        if (update.hasCallbackQuery())
            switch (update.getCallbackQuery().getData()) {
                case (Constant.Callback.GO_BACK) -> bot.popBotState(chatId);
                // case2..caseN
            }
        else if (update.hasMessage() && update.getMessage().hasText()) {
            Reminder tempReminder = bot.getTempReminders().get(chatId);
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
        } else
            bot.pushBotState(chatId, State.WRONG_INPUT_DATE);
    }

    @Override
    public State getType() {
        return State.ADD_REMINDER_TIME;
    }
}
