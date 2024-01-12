package com.schegolevalex.bot.reminderbot.controllers;

import com.schegolevalex.bot.reminderbot.ReminderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
@RequiredArgsConstructor
public class WebhookController {
    private final ReminderService reminderService;

    @PostMapping("/")
    public void getUpdate(@RequestBody Update update) {
        reminderService.perform(update);
    }
}
