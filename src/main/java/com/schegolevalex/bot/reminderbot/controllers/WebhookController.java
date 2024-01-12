package com.schegolevalex.bot.reminderbot.controllers;

import com.schegolevalex.bot.reminderbot.ReminderBot;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
@RequiredArgsConstructor
public class WebhookController {
    private final ReminderBot bot;

    @PostMapping("/")
    public BotApiMethod<?> getUpdate(@RequestBody Update update) {
        return bot.onWebhookUpdateReceived(update);
    }
}
