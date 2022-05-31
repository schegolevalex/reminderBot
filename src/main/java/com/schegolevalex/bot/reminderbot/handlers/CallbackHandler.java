package com.schegolevalex.bot.reminderbot.handlers;

import com.schegolevalex.bot.reminderbot.Constant;
import com.schegolevalex.bot.reminderbot.KeyboardFactory;
import com.schegolevalex.bot.reminderbot.entities.Reminder;
import com.schegolevalex.bot.reminderbot.services.ReminderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.util.AbilityUtils;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import java.util.List;
import java.util.Map;

@Component
public class CallbackHandler implements Handler {

    private ReminderServiceImpl reminderService;
    private Map<Long, UserState> userStates;

    @Autowired
    private CallbackHandler(ReminderServiceImpl reminderService, Map<Long, UserState> userStates) {
        this.reminderService = reminderService;
        this.userStates = userStates;
    }

    @Override
    public BotApiMethod<?> handle(Update update) {
        Long chatId = AbilityUtils.getChatId(update);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(AbilityUtils.getChatId(update)));

        switch (update.getCallbackQuery().getData()) {
            case (Constant.GO_TO_MY_REMINDERS):
                userStates.put(chatId, UserState.WATCHING_REMINDERS); //заглушка, осталось от старого, переписать TODO
                return allReminder(update);

            case (Constant.GO_TO_ADD_REMINDER):
                sendMessage.setText(Constant.REMINDER_DESCRIPTION_TEXT);
                userStates.put(chatId, UserState.ADDING_REMINDER_TEXT);
                break;

            default:
                sendMessage.setText(Constant.START_DESCRIPTION);
                sendMessage.setReplyMarkup(KeyboardFactory.withStartMessage());
                userStates.put(chatId, UserState.CHOOSING_FIRST_ACTION);
                break;
        }
        return sendMessage;
    }

    private BotApiMethod<?> allReminder(Update update) {
        List<Reminder> reminders = reminderService.getAllRemindersById(AbilityUtils.getChatId(update));
        StringBuilder text = new StringBuilder(Constant.MY_REMINDERS);
        for (Reminder reminder : reminders) {
            text.append(reminder.getDate() + " " + reminder.getTime() + " " + reminder.getText() + "\n");
        }
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(AbilityUtils.getChatId(update)));
        sendMessage.setText(String.valueOf(text));

        return sendMessage;
    }
}
