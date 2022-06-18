package com.schegolevalex.bot.reminderbot;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class KeyboardFactory {

    public static ReplyKeyboard withStartMessage() {
        InlineKeyboardMarkup replyKeyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        InlineKeyboardButton button1 = new InlineKeyboardButton("\uD83D\uDCC5 Мои напоминания");
        button1.setCallbackData(Constant.GO_TO_MY_REMINDERS);

        InlineKeyboardButton button2 = new InlineKeyboardButton("➕ Добавить напоминание");
        button2.setCallbackData(Constant.GO_TO_ADD_REMINDER);

        rowInline.add(button1);
        rowInline.add(button2);
        keyboard.add(rowInline);

        replyKeyboard.setKeyboard(keyboard);
        return replyKeyboard;
    }

    public static ReplyKeyboard withBackButton() {
        InlineKeyboardMarkup replyKeyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        InlineKeyboardButton button = new InlineKeyboardButton("⬅ Назад");
        button.setCallbackData(Constant.GO_BACK);
        rowInline.add(button);
        keyboard.add(rowInline);

        replyKeyboard.setKeyboard(keyboard);
        return replyKeyboard;
    }
}
