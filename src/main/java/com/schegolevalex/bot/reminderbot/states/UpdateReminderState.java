package com.schegolevalex.bot.reminderbot.states;

import org.telegram.telegrambots.bots.TelegramWebhookBot;

import java.util.Map;

public class UpdateReminderState extends UserState {

    public UpdateReminderState(TelegramWebhookBot bot) {
        super(bot);
    }

    @Override
    public void sendReply(Long chatId, Map<String, Integer> messageIds) {
    }
}
