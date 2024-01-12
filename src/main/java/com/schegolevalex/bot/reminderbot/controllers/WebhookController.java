package com.schegolevalex.bot.reminderbot.controllers;

import com.schegolevalex.bot.reminderbot.ReminderFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
@RequiredArgsConstructor
public class WebhookController {
    private final ReminderFacade reminderFacade;

    @PostMapping("/")
    public void getUpdate(@RequestBody Update update) {
        reminderFacade.perform(update);
    }
}
