package com.schegolevalex.bot.reminderbot.config;

public interface Constant {
    interface Message {
        String AWAIT_START_DESCRIPTION = "Для начала общения выберите команду /start";
        String CHOOSE_FIRST_ACTION_DESCRIPTION = "Выберите действие:";
        String ADD_REMINDER_TEXT_DESCRIPTION = "📝 Напишите, о чем Вам напомнить?";
        String EDIT_REMINDER_TEXT_DESCRIPTION = "📝 Предыдущий текст напоминания \"%s\".\nВведите новый текст для напоминания:";
        String ADD_REMINDER_DATE_DESCRIPTION = "📅 Когда Вам напомнить?\nВыберите дату:";
        String EDIT_REMINDER_DATE_DESCRIPTION = "📅 Предыдущая дата напоминания \"%s\".\nВыберите новую дату:";
        String ADD_REMINDER_TIME_DESCRIPTION = "🕙 Во сколько Вам напомнить?\nВыберите время:";
        String EDIT_REMINDER_TIME_DESCRIPTION = "🕙 Предыдущее время напоминания \"%s\".\nВыберите новое время:";
        String MY_REMINDERS = "Ваши напоминания:" + "\n";

        String UNKNOWN_REQUEST = "Не понимаю... Давайте заново.";
        String UNKNOWN_REMINDER = "Такого напоминания не найдено.";
        String DELETE_CONFIRMATION = "Вы уверены, что хотите удалить напоминание?";
        String SUCCESSFUL_EDITING = "Напоминание отредактировано.";
        String SUCCESSFUL_DELETION = "Напоминание удалено.";
        String SUCCESSFUL_ADDITION = "Напоминание c текстом \"%s\" успешно создано. Напомню %s в %s";
        String WRONG_DATE_FORMAT = "Не удалось распознать дату, повторите ввод";
        String WRONG_TIME_FORMAT = "Не удалось распознать время, повторите ввод";

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
        String EMPTY = "/empty";
        String[] DAYS = new String[]{"/monday", "/tuesday", "/wednesday", "/thursday", "/friday", "/saturday", "/sunday"};

        String DATE = "/date/";
        String CHANGE_MONTH = "/previousMonth/";
        String TIME = "/time/";
        String CHANGE_TIME = "/changeTime/";
        String CHANGE_TIME_BACKSPACE = "/changeTimeBackspace/";
    }

    interface Button {
        String MY_REMINDERS = "⏰ Мои напоминания";
        String ADD_REMINDER = "➕ Добавить напоминание";
        String BACK = "⬅ Назад";
        String MAIN_PAGE = "🏠 На главную";
        String EDIT_REMINDER_TEXT = "✏️ Изменить текст";
        String EDIT_REMINDER_DATE = "📝 Изменить дату";
        String EDIT_REMINDER_TIME = "🕙 Изменить время";
        String DELETE_REMINDER = "❌ Удалить";
        String CONFIRM_TO_DELETE_REMINDER = "🆗 Да, удалить напоминание";
        String OK = "OK";
        String[] DAYS_OF_WEEK = new String[]{"ПН", "ВТ", "СР", "ЧТ", "ПТ", "СБ", "ВС"};
    }

    interface Command {
        String START = "/start";
    }
}
