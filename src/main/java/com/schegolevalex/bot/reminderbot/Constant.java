package com.schegolevalex.bot.reminderbot;

public interface Constant {
//    String CHAT_STATES = "Состояния чата";

    String AWAIT_START_DESCRIPTION = "Для начала общения выберите команду /start";
    String CHOOSE_FIRST_ACTION_DESCRIPTION = "Выберите действие:";
    String ADD_REMINDER_TEXT_DESCRIPTION = "1️⃣ Напишите, о чем Вам напомнить?";
    String ADD_REMINDER_DATE_DESCRIPTION = "2️⃣ Когда Вам напомнить? Введите дату в формате \"ДД.ММ.ГГГГ\":";
    String ADD_REMINDER_TIME_DESCRIPTION = "3️⃣ Во сколько Вам напомнить? Введите время в формате \"ЧЧ:ММ\":";
    String MY_REMINDERS = "Ваши напоминания:" + "\n";


    //    для колбэков кнопок
    String GO_TO_MAIN = "Переход в /main";
    String GO_TO_MY_REMINDERS = "Мои напоминания /reminders";
    String GO_TO_ADD_REMINDER = "Добавить напоминание /addReminder";
    String GO_TO_UPDATE_REMINDER = "Изменить напоминание /updateReminder";
    String GO_TO_DELETE_REMINDER = "Удалить напоминание /deleteReminder";
    String GO_BACK = "Назад";

    String UNKNOWN_REQUEST = "Не понимаю... Давайте заново. Для начала общения выберите команду /start";
    String SUCCESSFUL_ADDITION = "Напоминание c текстом \"%s\" успешно создано. Напомню %s в %s";
    String WRONG_DATE_FORMAT = "Не удалось распознать дату, повторите ввод. Напоминаю, формат даты \"ДД.ММ.ГГГГ\":";
    String WRONG_TIME_FORMAT = "Не удалось распознать время, повторите ввод. Напоминаю, формат времени \"ЧЧ:ММ\":";

    String REMINDER_LIST_IS_EMPTY = "у Вас нет напоминаний.";
    String REMINDER_MESSAGE = "Сегодня в %s Вы просили напомнить \"%s\"";

    String START_DESCRIPTION = "Привет, я \"TO-DO\" бот. Я помогу не забыть о важных вещах. " +
            "Для начала общения введите команду /start";
}
