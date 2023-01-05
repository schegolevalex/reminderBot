package com.schegolevalex.bot.reminderbot;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class KeyboardFactory {

//    public static InlineKeyboardMarkup withFirstActionMessage() {
//        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup();
//        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
//        List<InlineKeyboardButton> rowInline = new ArrayList<>();
//
//        InlineKeyboardButton button1 = new InlineKeyboardButton("\uD83D\uDCC5 Мои напоминания");
//        button1.setCallbackData(Constant.GO_TO_MY_REMINDERS);
//
//        InlineKeyboardButton button2 = new InlineKeyboardButton("➕ Добавить напоминание");
//        button2.setCallbackData(Constant.GO_TO_ADD_REMINDER);
//
//        rowInline.add(button1);
//        rowInline.add(button2);
//        keyboard.add(rowInline);
//
//        inlineKeyboard.setKeyboard(keyboard);
//        return inlineKeyboard;
//    }

    public static InlineKeyboardMarkup withFirstActionMessage() {
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        List<InlineKeyboardButton> row3 = new ArrayList<>();
        List<InlineKeyboardButton> row4 = new ArrayList<>();

        InlineKeyboardButton button1 = new InlineKeyboardButton("\uD83D\uDCC5 Мои напоминания");
        button1.setCallbackData(Constant.GO_TO_MY_REMINDERS);

        InlineKeyboardButton button2 = new InlineKeyboardButton("➕ Добавить напоминание");
        button2.setCallbackData(Constant.GO_TO_ADD_REMINDER);

        InlineKeyboardButton button3 = new InlineKeyboardButton("✏️Изменить напоминание");
        button3.setCallbackData(Constant.GO_TO_UPDATE_REMINDER);

        InlineKeyboardButton button4 = new InlineKeyboardButton("❌ Удалить напоминание");
        button4.setCallbackData(Constant.GO_TO_DELETE_REMINDER);

        row1.add(button1);
        row2.add(button2);
        row3.add(button3);
        row4.add(button4);

        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);
        keyboard.add(row4);

        inlineKeyboard.setKeyboard(keyboard);
        return inlineKeyboard;
    }

    public static InlineKeyboardMarkup withBackButton() {
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        InlineKeyboardButton button = new InlineKeyboardButton("⬅ Назад");
        button.setCallbackData(Constant.GO_BACK);
        rowInline.add(button);
        keyboard.add(rowInline);

        inlineKeyboard.setKeyboard(keyboard);
        return inlineKeyboard;
    }
}
