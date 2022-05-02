package com.schegolevalex.bot.reminderbot.controllers;

import com.schegolevalex.bot.reminderbot.ReminderBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
public class WebhookController {
    @Autowired
    private ReminderBot reminderBot;

    @PostMapping("/")
    public BotApiMethod<?> getUpdate(@RequestBody Update update) {
        return reminderBot.onWebhookUpdateReceived(update);
    }
}
