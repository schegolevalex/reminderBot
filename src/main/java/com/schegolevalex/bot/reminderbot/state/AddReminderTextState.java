package com.schegolevalex.bot.reminderbot.state;

import com.schegolevalex.bot.reminderbot.Constant;
import com.schegolevalex.bot.reminderbot.KeyboardFactory;
import com.schegolevalex.bot.reminderbot.ReminderBot;
import com.schegolevalex.bot.reminderbot.entity.Reminder;
import com.schegolevalex.bot.reminderbot.handler.Handler;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.util.AbilityUtils;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;

@Component
public class AddReminderTextState extends AbstractState {

    private final AddReminderDateState addReminderDateState;

    @Autowired
    public AddReminderTextState(Map<String, Handler> handlerMap,
                                ReminderBot bot, AddReminderDateState addReminderDateState) {
        super(handlerMap, bot);
        this.addReminderDateState = addReminderDateState;
    }

    @Override
    public void handle(Update update) {
        Long chatId = AbilityUtils.getChatId(update);

        Reminder tempReminder = new Reminder();
        tempReminder.setChatId(chatId);
        tempReminder.setText(update.getMessage().getText());
        getBot().getTempReminders().put(chatId, tempReminder);
        getBot().pushBotState(chatId, addReminderDateState);
    }

    @SneakyThrows
    @Override
    public BotApiMethod<?> reply(Update update) {
        return EditMessageText.builder()
                .chatId(AbilityUtils.getChatId(update))
                .messageId(update.getMessage().getMessageId())
                .text(Constant.ADD_REMINDER_TEXT_DESCRIPTION)
                .replyMarkup(KeyboardFactory.withBackButton())
                .build();
    }
}