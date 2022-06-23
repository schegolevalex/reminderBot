package com.schegolevalex.bot.reminderbot.controllers;

import com.schegolevalex.bot.reminderbot.ReminderBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
public class WebhookController {
    private final ReminderBot reminderBot;

    @Autowired
    public WebhookController(ReminderBot reminderBot) {
        this.reminderBot = reminderBot;
    }

    @PostMapping("/")
    public void getUpdate(@RequestBody Update update) {
        reminderBot.onWebhookUpdateReceived(update);
    }
}
