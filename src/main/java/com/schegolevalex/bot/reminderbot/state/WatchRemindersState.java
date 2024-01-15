package com.schegolevalex.bot.reminderbot.state;

import com.schegolevalex.bot.reminderbot.ReminderBot;
import com.schegolevalex.bot.reminderbot.services.ReminderService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class WatchRemindersState extends AbstractState {
    private final ReminderService reminderService;

    public WatchRemindersState(@Lazy ReminderBot bot,
                               ReminderService reminderService) {
        super(bot);
        this.reminderService = reminderService;
    }

    @Override
    public void handle(Update update) {

    }

    @Override
    public BotApiMethod<?> reply(Update update) {
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
//        return editMessageText;
    }

    @Override
    public State getType() {
        return State.WATCH_REMINDERS;
    }
}
