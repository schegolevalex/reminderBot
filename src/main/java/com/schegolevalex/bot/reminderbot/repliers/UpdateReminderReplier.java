package com.schegolevalex.bot.reminderbot.repliers;

import org.telegram.telegrambots.bots.TelegramWebhookBot;

import java.util.Map;

public class UpdateReminderReplier extends AbstractReplier {

    public UpdateReminderReplier(TelegramWebhookBot bot) {
        super(bot);
    }

    @Override
    public void reply(Long chatId, Map<String, Integer> messageIds) {
    }
}
