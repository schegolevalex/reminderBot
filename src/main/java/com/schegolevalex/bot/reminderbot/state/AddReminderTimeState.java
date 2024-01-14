package com.schegolevalex.bot.reminderbot.state;

import com.schegolevalex.bot.reminderbot.Constant;
import com.schegolevalex.bot.reminderbot.KeyboardFactory;
import com.schegolevalex.bot.reminderbot.ReminderBot;
import com.schegolevalex.bot.reminderbot.entity.Reminder;
import com.schegolevalex.bot.reminderbot.handler.Handler;
import com.schegolevalex.bot.reminderbot.services.ReminderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.util.AbilityUtils;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;

@Component
public class AddReminderTimeState extends AbstractState {
    private final ReminderService reminderService;
    private final WrongInputTimeState wrongInputTimeState;
    private final SuccessfulAdditionState successfulAdditionState;

    @Autowired
    public AddReminderTimeState(Map<String, Handler> handlerMap,
                                ReminderBot bot,
                                ReminderService reminderService,
                                WrongInputTimeState wrongInputTimeState,
                                SuccessfulAdditionState successfulAdditionState) {
        super(handlerMap, bot);
        this.reminderService = reminderService;
        this.wrongInputTimeState = wrongInputTimeState;
        this.successfulAdditionState = successfulAdditionState;
    }

    @Override
    public void handle(Update update) {
        Long chatId = AbilityUtils.getChatId(update);

        Reminder tempReminder = getBot().getTempReminders().get(chatId);
        String text = update.getMessage().getText();
        LocalTime time;
        try {
            time = LocalTime.parse(text);
        } catch (DateTimeParseException e) {
            getBot().pushBotState(chatId, wrongInputTimeState);
            return;
        }
        tempReminder.setTime(time);

        reminderService.saveReminder(tempReminder);
        reminderService.sendReminder(tempReminder);
        getBot().pushBotState(chatId, successfulAdditionState);
    }

    @Override
    public BotApiMethod<?> reply(Update update) {
        Long chatId = AbilityUtils.getChatId(update);
        return EditMessageText.builder()
                .chatId(chatId)
                .messageId(update.getMessage().getMessageId())
                .text("Текст: \"" + getBot().getTempReminders().get(chatId).getText() + "\"\n" +
                        "Дата: " + getBot().getTempReminders().get(chatId).getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + "\"\n" +
                        Constant.ADD_REMINDER_TIME_DESCRIPTION)
                .replyMarkup(KeyboardFactory.withBackButton())
                .build();
    }
}
