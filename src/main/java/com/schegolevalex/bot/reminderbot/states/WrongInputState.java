package com.schegolevalex.bot.reminderbot.states;

import com.schegolevalex.bot.reminderbot.Constant;
import com.schegolevalex.bot.reminderbot.ReminderFacade;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import java.util.Map;

@Component
public class WrongInputState extends UserState {

    private final ReminderFacade reminderFacade;

    public WrongInputState(TelegramWebhookBot bot, ReminderFacade reminderFacade) {
        super(bot);
        this.reminderFacade = reminderFacade;
    }

    @SneakyThrows
    @Override
    public void sendReply(Long chatId, Map<String, Integer> messageIds) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(String.valueOf(chatId));

        reminderFacade.getCurrentStateStack(chatId).pop();
        String previousState = reminderFacade.getCurrentStateStack(chatId).peek().getClass().getSimpleName();

        switch (previousState) {
            case ("AddingReminderDateState") -> editMessageText.setText(Constant.WRONG_DATE_FORMAT);
            case ("AddingReminderTimeState") -> editMessageText.setText(Constant.WRONG_TIME_FORMAT);
            default -> editMessageText.setText(Constant.UNKNOWN_REQUEST);
        }
        bot.execute(editMessageText);
    }
}
