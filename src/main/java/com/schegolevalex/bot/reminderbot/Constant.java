package com.schegolevalex.bot.reminderbot;

public interface Constant {
    String AWAIT_START_DESCRIPTION = "Для начала общения выберите команду /start";
    String CHOOSE_FIRST_ACTION_DESCRIPTION = "Выберите действие:";
    String ADD_REMINDER_TEXT_DESCRIPTION = "1️⃣ Напишите, о чем Вам напомнить?";
    String ADD_REMINDER_DATE_DESCRIPTION = "2️⃣ Когда Вам напомнить? Введите дату в формате \"ДД.ММ.ГГГГ\":";
    String ADD_REMINDER_TIME_DESCRIPTION = "3️⃣ Во сколько Вам напомнить? Введите время в формате \"ЧЧ:ММ\":";
    String MY_REMINDERS = "Ваши напоминания:" + "\n";

    String UNKNOWN_REQUEST = "Не понимаю... Давайте заново.";
    String UNKNOWN_REMINDER = "Такого напоминания не найдено.";
    String SUCCESSFUL_ADDITION = "Напоминание c текстом \"%s\" успешно создано. Напомню %s в %s";
    String WRONG_DATE_FORMAT = "Не удалось распознать дату, повторите ввод. Напоминаю, формат даты \"ДД.ММ.ГГГГ\":";
    String WRONG_TIME_FORMAT = "Не удалось распознать время, повторите ввод. Напоминаю, формат времени \"ЧЧ:ММ\":";

    String REMINDER_LIST_IS_EMPTY = "У Вас нет напоминаний.";
    String REMINDER_MESSAGE = "Сегодня в %s Вы просили напомнить \"%s\"";

    String START_DESCRIPTION = "Привет, я ToDo-бот. Я помогу не забыть о важных вещах. " +
            "Для начала общения нажмите кнопку \"ЗАПУСТИТЬ\"";

    interface Callback {
        String GO_TO_MAIN = "/main";
        String GO_TO_MY_REMINDERS = "/reminders";
        String GO_TO_MY_REMINDER = "/reminders/";
        String GO_TO_ADD_REMINDER = "/addReminder";
        String GO_TO_EDIT_REMINDER = "/updateReminder";
        String GO_TO_DELETE_REMINDER = "/deleteReminder";
        String GO_BACK = "/back";
    }
}
