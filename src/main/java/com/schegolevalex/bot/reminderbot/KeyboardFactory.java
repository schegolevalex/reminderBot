package com.schegolevalex.bot.reminderbot;

import com.schegolevalex.bot.reminderbot.entity.Reminder;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class KeyboardFactory {

    private final static InlineKeyboardButton MY_REMINDERS_BUTTON = createButton("📅 Мои напоминания", Constant.Callback.GO_TO_MY_REMINDERS);
    private final static InlineKeyboardButton CREATE_REMINDER_BUTTON = createButton("➕ Добавить напоминание", Constant.Callback.GO_TO_ADD_REMINDER);
    private final static InlineKeyboardButton UPDATE_REMINDER_BUTTON = createButton("✏️Изменить напоминание", Constant.Callback.GO_TO_EDIT_REMINDER);
    private final static InlineKeyboardButton REMOVE_REMINDER_BUTTON = createButton("❌ Удалить напоминание", Constant.Callback.GO_TO_DELETE_REMINDER);
    private final static InlineKeyboardButton BACK_BUTTON = createButton("⬅ Назад", Constant.Callback.GO_BACK);
    private final static InlineKeyboardButton MAIN_PAGE_BUTTON = createButton("🏠 На главную", Constant.Callback.GO_TO_MAIN);


    public static InlineKeyboardMarkup withFirstActionMessage() {
        List<InlineKeyboardButton> row1 = List.of(MY_REMINDERS_BUTTON);
        List<InlineKeyboardButton> row2 = List.of(CREATE_REMINDER_BUTTON);

        return new InlineKeyboardMarkup(List.of(row1, row2));
    }


    public static InlineKeyboardMarkup withRemindersMessage(List<Reminder> reminders) {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        reminders.stream()
                .sorted()
                .map(reminder -> {
                    LocalDateTime reminderDateTime = reminder.getDate().atTime(reminder.getTime());
                    String reminderText = reminderDateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:SS")) + " " + reminder.getText();

                    if (reminderDateTime.isAfter(LocalDateTime.now()))
                        reminderText = "⏳ " + reminderText;
                    else
                        reminderText = "✅ " + reminderText;

                    return createButton(reminderText, Constant.Callback.GO_TO_MY_REMINDER + reminder.getId().toString());
                })
                .map(List::of)
                .forEach(keyboard::add);
        keyboard.add(List.of(BACK_BUTTON));

        return new InlineKeyboardMarkup(keyboard);
    }

    public static InlineKeyboardMarkup withReminderMessage() {
        List<InlineKeyboardButton> row1 = List.of(UPDATE_REMINDER_BUTTON);
        List<InlineKeyboardButton> row2 = List.of(REMOVE_REMINDER_BUTTON);
        List<InlineKeyboardButton> row3 = List.of(BACK_BUTTON);

        return new InlineKeyboardMarkup(List.of(row1, row2, row3));
    }


    public static InlineKeyboardMarkup withBackButton() {
        return new InlineKeyboardMarkup(List.of(List.of(BACK_BUTTON)));
    }

    public static InlineKeyboardMarkup withMainPageButton() {
        return new InlineKeyboardMarkup(List.of(List.of(MAIN_PAGE_BUTTON)));
    }

    private static InlineKeyboardButton createButton(String text, String callback) {
        InlineKeyboardButton button = new InlineKeyboardButton(text);
        button.setCallbackData(callback);
        return button;
    }
}
