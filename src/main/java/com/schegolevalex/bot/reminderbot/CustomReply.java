package com.schegolevalex.bot.reminderbot;

import lombok.Builder;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Builder
public record CustomReply(String text, InlineKeyboardMarkup replyMarkup) {
}
