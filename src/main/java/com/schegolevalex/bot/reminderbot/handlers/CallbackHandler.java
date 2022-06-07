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
import java.util.Stack;

@Component
public class CallbackHandler implements Handler {

    private final ReminderServiceImpl reminderService;

    @Autowired
    private CallbackHandler(ReminderServiceImpl reminderService) {
        this.reminderService = reminderService;
    }

    @Override
    public BotApiMethod<?> handle(Update update, Stack<UserState> userState) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(AbilityUtils.getChatId(update)));

        switch (update.getCallbackQuery().getData()) {
            case (Constant.GO_TO_MY_REMINDERS):
                userState.push(UserState.WATCHING_REMINDERS);
                return allReminder(update);

            case (Constant.GO_TO_ADD_REMINDER):
                sendMessage.setText(Constant.REMINDER_DESCRIPTION_TEXT);
                userState.push(UserState.ADDING_REMINDER_TEXT);
                break;

            default:
//                TODO избавиться от дублирования кода
                sendMessage.setText(Constant.START_DESCRIPTION);
                sendMessage.setReplyMarkup(KeyboardFactory.withStartMessage());
                userState.push(UserState.CHOOSING_FIRST_ACTION);
                break;
        }
        return sendMessage;
    }

    private BotApiMethod<?> allReminder(Update update) {
        List<Reminder> reminders = reminderService.getAllRemindersById(AbilityUtils.getChatId(update));
        StringBuilder text = new StringBuilder(Constant.MY_REMINDERS);
        for (Reminder reminder : reminders) {
            text.append(reminder.getDate())
                    .append(" ")
                    .append(reminder.getTime())
                    .append(" ")
                    .append(reminder.getText())
                    .append("\n");
        }

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(AbilityUtils.getChatId(update)));
        sendMessage.setText(String.valueOf(text));

        return sendMessage;
    }
}
