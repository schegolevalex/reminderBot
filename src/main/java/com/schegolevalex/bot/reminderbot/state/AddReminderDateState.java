package com.schegolevalex.bot.reminderbot.state;

import com.schegolevalex.bot.reminderbot.Constant;
import com.schegolevalex.bot.reminderbot.KeyboardFactory;
import com.schegolevalex.bot.reminderbot.ReminderBot;
import com.schegolevalex.bot.reminderbot.entity.Reminder;
import com.schegolevalex.bot.reminderbot.handler.Handler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.util.AbilityUtils;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;

@Component
public class AddReminderDateState extends AbstractState {

    private final AddReminderTimeState addReminderTimeState;
    private final WrongInputDateState wrongInputDateState;

    @Autowired
    public AddReminderDateState(Map<String, Handler> handlerMap,
                                ReminderBot bot,
                                AddReminderTimeState addReminderDateState,
                                WrongInputDateState wrongInputDateState) {
        super(handlerMap, bot);
        this.addReminderTimeState = addReminderDateState;
        this.wrongInputDateState = wrongInputDateState;
    }

    @Override
    public void handle(Update update) {
        Long chatId = AbilityUtils.getChatId(update);

        Reminder tempReminder = getBot().getTempReminders().get(chatId);
        String text = update.getMessage().getText();
        LocalDate date;
        try {
            date = LocalDate.parse(text, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        } catch (DateTimeParseException e) {
            getBot().pushBotState(chatId, wrongInputDateState);
            return;
        }
        tempReminder.setDate(date);
        getBot().pushBotState(chatId, addReminderTimeState);
    }

    @Override
    public BotApiMethod<?> reply(Update update) {
        return EditMessageText.builder()
                .chatId(AbilityUtils.getChatId(update))
                .messageId(update.getMessage().getMessageId())
                .text("Текст: \"" + getBot().getTempReminders().get(AbilityUtils.getChatId(update)).getText() +
                        "\"\n" + Constant.ADD_REMINDER_DATE_DESCRIPTION)
                .replyMarkup(KeyboardFactory.withBackButton())
                .build();
    }
}
