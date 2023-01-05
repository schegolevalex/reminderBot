package com.schegolevalex.bot.reminderbot.controllers;

import com.schegolevalex.bot.reminderbot.ReminderFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
public class WebhookController {
    private final ReminderFacade facade;

    @Autowired
    public WebhookController(ReminderFacade facade) {
        this.facade = facade;
    }

    @PostMapping("/")
    public void getUpdate(@RequestBody Update update) {
        facade.getResult(update);
    }
}
