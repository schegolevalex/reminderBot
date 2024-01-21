package com.schegolevalex.bot.reminderbot.config;

public interface Constant {
    interface Message {
        String AWAIT_START_DESCRIPTION = "Для начала общения выберите команду /start";
        String CHOOSE_FIRST_ACTION_DESCRIPTION = "Выберите действие:";
        String ADD_REMINDER_TEXT_DESCRIPTION = "📝 Напишите, о чем Вам напомнить?";
        String EDIT_REMINDER_TEXT_DESCRIPTION = "📝 Предыдущий текст напоминания \"%s\".\nВведите новый текст для напоминания:";
        String ADD_REMINDER_DATE_DESCRIPTION = "📅 Когда Вам напомнить?\nВведите дату в формате \"ДД.ММ.ГГГГ\":";
        String EDIT_REMINDER_DATE_DESCRIPTION = "📅 Предыдущая дата напоминания \"%s\".\nВведите новую дату для напоминания в формате \"ДД.ММ.ГГГГ\":";
        String ADD_REMINDER_TIME_DESCRIPTION = "🕙 Во сколько Вам напомнить?\nВведите время в формате \"ЧЧ:ММ\":";
        String EDIT_REMINDER_TIME_DESCRIPTION = "🕙 Предыдущее время напоминания \"%s\".\nВведите новое время для напоминания в формате \"ЧЧ:ММ\":";
        String MY_REMINDERS = "Ваши напоминания:" + "\n";

        String UNKNOWN_REQUEST = "Не понимаю... Давайте заново.";
        String UNKNOWN_REMINDER = "Такого напоминания не найдено.";
        String DELETE_CONFIRMATION = "Вы уверены, что хотите удалить напоминание?";
        String SUCCESSFUL_EDITING = "Напоминание отредактировано.";
        String SUCCESSFUL_DELETION = "Напоминание удалено.";
        String SUCCESSFUL_ADDITION = "Напоминание c текстом \"%s\" успешно создано. Напомню %s в %s";
        String WRONG_DATE_FORMAT = "Не удалось распознать дату, повторите ввод. Напоминаю, формат даты \"ДД.ММ.ГГГГ\":";
        String WRONG_TIME_FORMAT = "Не удалось распознать время, повторите ввод. Напоминаю, формат времени \"ЧЧ:ММ\":";

        String REMINDER_LIST_IS_EMPTY = "У Вас нет напоминаний.";
        String REMINDER_MESSAGE = "Сегодня в %s Вы просили напомнить \"%s\"";

        String START_DESCRIPTION = "Привет, я ToDo-бот. Я помогу не забыть о важных вещах. " +
                "Для начала общения нажмите кнопку \"Начать\" или напишите команду \"/start\"";

        String START_COMMAND_DESCRIPTION = "нажми для начала общения";
    }

    interface Callback {
        String GO_TO_MAIN = "/main";
        String GO_TO_MY_REMINDERS = "/reminders";
        String GO_TO_MY_REMINDER = "/reminders/";
        String GO_TO_ADD_REMINDER = "/addReminder";
        String GO_TO_EDIT_REMINDER_TEXT = "/editReminderText/";
        String GO_TO_EDIT_REMINDER_DATE = "/editReminderDate/";
        String GO_TO_EDIT_REMINDER_TIME = "/editReminderTime/";
        String GO_TO_CONFIRM_TO_DELETE_REMINDER = "/confirmToDeleteReminder/";
        String GO_TO_CONFIRMED_DELETION = "/confirmedDeletion/";
        String GO_BACK = "/back";
        String OK = "/ok";
    }

    interface Button {
        String MY_REMINDERS = "⏰ Мои напоминания";
        String ADD_REMINDER = "➕ Добавить напоминание";
        String BACK = "⬅ Назад";
        String MAIN_PAGE = "🏠 На главную";
        String EDIT_REMINDER_TEXT = "✏️ Изменить текст напоминания";
        String EDIT_REMINDER_DATE = "📝 Изменить дату напоминания";
        String EDIT_REMINDER_TIME = "🕙 Изменить время напоминания";
        String DELETE_REMINDER = "❌ Удалить напоминание";
        String CONFIRM_TO_DELETE_REMINDER = "Да, удалить напоминание";
        String OK = "OK";
    }
}
