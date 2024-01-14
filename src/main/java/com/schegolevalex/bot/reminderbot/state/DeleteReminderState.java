package com.schegolevalex.bot.reminderbot.state;

import com.schegolevalex.bot.reminderbot.ReminderBot;
import com.schegolevalex.bot.reminderbot.handler.Handler;
import com.schegolevalex.bot.reminderbot.services.ReminderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.util.AbilityUtils;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;

@Component
public class DeleteReminderState extends AbstractState {
    private final ReminderService reminderService;
    private final WatchRemindersState watchRemindersState;

    @Autowired
    public DeleteReminderState(Map<String, Handler> handlerMap,
                               ReminderBot bot,
                               ReminderService reminderService,
                               WatchRemindersState watchRemindersState) {
        super(handlerMap, bot);
        this.reminderService = reminderService;
        this.watchRemindersState = watchRemindersState;
    }


    @Override
    public void handle(Update update) {
        Long chatId = AbilityUtils.getChatId(update);
        reminderService.deleteReminder(chatId);
        getBot().pushBotState(chatId, watchRemindersState);
    }

    @Override
    public BotApiMethod<?> reply(Update update) {
        // todo
        return null;
//        EditMessageText editMessageText = new EditMessageText();
//        editMessageText.setChatId(String.valueOf(chatId));
//
//        List<Reminder> reminders = reminderService.getAllRemindersById(chatId);
//        reminders = reminders.stream().sorted().toList();
//
//        StringBuilder text = new StringBuilder(Constant.MY_REMINDERS);
//
//        if (reminders.isEmpty()) {
//            text.append(Constant.REMINDER_LIST_IS_EMPTY);
//        } else {
//            LocalDateTime now = LocalDateTime.now();
//            for (Reminder reminder : reminders) {
//                LocalDateTime reminderDateTime = reminder.getDate().atTime(reminder.getTime());
//                if (now.isBefore(reminderDateTime)) {
//                    text.append("⏳ ");
//                } else {
//                    text.append("✅ ");
//                }
//
//                text.append(reminder.getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
//                        .append(" ")
//                        .append(reminder.getTime())
//                        .append(" ")
//                        .append(reminder.getText())
//                        .append("\n");
//            }
//        }
//        editMessageText.setText(String.valueOf(text));
//        editMessageText.setReplyMarkup(KeyboardFactory.withBackButton());
//        editMessageText.setMessageId(messageIds.get(String.valueOf(chatId)));
    }
}
