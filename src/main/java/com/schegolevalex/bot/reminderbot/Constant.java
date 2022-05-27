package com.schegolevalex.bot.reminderbot;

public interface Constant {
//    String BOT_NAME = "First_To_Do_Bot";
//    String BOT_TOKEN = System.getenv("BOT_TOKEN");
//    String DB_URL = System.getenv("DB_URL");
//    String DB_USERNAME = System.getenv("DB_USERNAME");
//    String DB_PASSWORD = System.getenv("DB_PASSWORD");

    String CHAT_STATES = "Состояния чата";

    String START_DESCRIPTION = "Выберите действие:";
    String REMINDER_DESCRIPTION_TEXT = "Напишите, о чем Вам напомнить?";
    String REMINDER_DESCRIPTION_DATE = "Когда Вам напомнить? Введите дату в формате ГГГГ.ММ.ДД:";
    String REMINDER_DESCRIPTION_TIME = "Во сколько Вам напомнить? Введите время в формате ЧЧ:ММ:CC";


    //    для колбэков кнопок
    String GO_TO_MAIN = "Переход в /main";
    String GO_TO_MY_REMINDERS = "Мои напоминания /reminders";
    String GO_TO_ADD_REMINDER = "Добавить напоминание /addReminder";

    String UNKNOWN_REQUEST = "Не понимаю... Давайте заново.";
    String SUCCESSFUL_ADDITION = "Напоминание успешно создано";
}
