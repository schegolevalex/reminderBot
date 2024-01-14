package com.schegolevalex.bot.reminderbot.state;

import org.telegram.telegrambots.bots.TelegramWebhookBot;

import java.util.Map;

public class UpdateReminderState extends AbstractState {

    public UpdateReminderState(TelegramWebhookBot bot) {
        super(bot);
    }

    @Override
    public void reply(Long chatId, Map<String, Integer> messageIds) {
    }
}
