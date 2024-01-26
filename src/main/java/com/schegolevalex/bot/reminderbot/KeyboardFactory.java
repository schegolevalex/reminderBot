package com.schegolevalex.bot.reminderbot;

import com.schegolevalex.bot.reminderbot.entity.Reminder;
import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static com.schegolevalex.bot.reminderbot.config.Constant.Button;
import static com.schegolevalex.bot.reminderbot.config.Constant.Callback;

public class KeyboardFactory {

    private final static InlineKeyboardButton MY_REMINDERS_BUTTON = createButton(Button.MY_REMINDERS, Callback.GO_TO_MY_REMINDERS);
    private final static InlineKeyboardButton CREATE_REMINDER_BUTTON = createButton(Button.ADD_REMINDER, Callback.GO_TO_ADD_REMINDER);
    private final static InlineKeyboardButton BACK_BUTTON = createButton(Button.BACK, Callback.GO_BACK);
    private final static InlineKeyboardButton OK_BUTTON = createButton(Button.OK, Callback.OK);
    private final static InlineKeyboardButton MAIN_PAGE_BUTTON = createButton(Button.MAIN_PAGE, Callback.GO_TO_MAIN);

    private final static InlineKeyboardButton EMPTY_BUTTON = createButton(" ", Callback.EMPTY);

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
                    String reminderText = reminder.toUserView();

                    if (reminderDateTime.isAfter(LocalDateTime.now()))
                        reminderText = "⏳ " + reminderText;
                    else
                        reminderText = "✅ " + reminderText;

                    return createButton(reminderText, Callback.GO_TO_MY_REMINDER + reminder.getId().toString());
                })
                .map(List::of)
                .forEach(keyboard::add);
        keyboard.add(List.of(BACK_BUTTON));

        return new InlineKeyboardMarkup(keyboard);
    }

    public static InlineKeyboardMarkup withReminderMessage(Reminder reminder) {
        List<InlineKeyboardButton> row1 = List.of(createButton(Button.EDIT_REMINDER_TEXT, Callback.GO_TO_EDIT_REMINDER_TEXT + reminder.getId()));
        List<InlineKeyboardButton> row2 = List.of(createButton(Button.EDIT_REMINDER_DATE, Callback.GO_TO_EDIT_REMINDER_DATE + reminder.getId()));
        List<InlineKeyboardButton> row3 = List.of(createButton(Button.EDIT_REMINDER_TIME, Callback.GO_TO_EDIT_REMINDER_TIME + reminder.getId()));
        List<InlineKeyboardButton> row4 = List.of(createButton(Button.DELETE_REMINDER, Callback.GO_TO_CONFIRM_TO_DELETE_REMINDER + reminder.getId()));
        List<InlineKeyboardButton> row5 = List.of(BACK_BUTTON);

        return new InlineKeyboardMarkup(List.of(row1, row2, row3, row4, row5));
    }

    public static InlineKeyboardMarkup withDeleteReminderConfirmation(Reminder reminder) {
        List<InlineKeyboardButton> row1 = List.of(createButton(Button.CONFIRM_TO_DELETE_REMINDER, Callback.GO_TO_CONFIRMED_DELETION + reminder.getId()));
        List<InlineKeyboardButton> row2 = List.of(BACK_BUTTON);

        return new InlineKeyboardMarkup(List.of(row1, row2));
    }

    public static InlineKeyboardMarkup withBackButton() {
        return new InlineKeyboardMarkup(List.of(List.of(BACK_BUTTON)));
    }

    public static InlineKeyboardMarkup withOkButton() {
        return new InlineKeyboardMarkup(List.of(List.of(OK_BUTTON)));
    }

    public static InlineKeyboardMarkup withMainPageButton() {
        return new InlineKeyboardMarkup(List.of(List.of(MAIN_PAGE_BUTTON)));
    }

    public static InlineKeyboardMarkup withCalendar(LocalDate targetDate) {
        // first row
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        row1.add(createButton("<<", Callback.CHANGE_MONTH + targetDate.minusMonths(1)));
        row1.add(createButton(targetDate.format(DateTimeFormatter
//                .ofLocalizedDate(FormatStyle.SHORT)
                .ofPattern("LLLL yyyy")
                .localizedBy(Locale.forLanguageTag("ru"))), Callback.EMPTY));
        row1.add(createButton(">>", Callback.CHANGE_MONTH + targetDate.plusMonths(1)));

        // second row, days of week
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        for (int i = 0; i < Button.DAYS_OF_WEEK.length; i++) {
            row2.add(createButton(Button.DAYS_OF_WEEK[i], Callback.DAYS[i]));
        }

        // calendar rows
        LocalDate firstDayOfMonth = targetDate.withDayOfMonth(1);
        LocalDate lastDayOfMonth = targetDate.withDayOfMonth(targetDate.lengthOfMonth());
        List<InlineKeyboardButton> buttons = firstDayOfMonth.datesUntil(lastDayOfMonth.plusDays(1))
                .map(date -> createButton(date.format(DateTimeFormatter.ofPattern("d")), Callback.DATE + date))
                .collect(Collectors.toList());
        int firstDayOfMonthValue = firstDayOfMonth.getDayOfWeek().getValue();
        for (int i = 0; i < firstDayOfMonthValue - 1; i++)
            buttons.addFirst(EMPTY_BUTTON);

        int lastDayOfMonthValue = lastDayOfMonth.getDayOfWeek().getValue();
        for (int i = 0; i < 7 - lastDayOfMonthValue; i++)
            buttons.addLast(EMPTY_BUTTON);

        List<List<InlineKeyboardButton>> calendarRows = new ArrayList<>();
        for (int i = 0; i < buttons.size() / 7; i++) {
            calendarRows.add(buttons.subList(i * 7, i * 7 + 7));
        }

        // quick pick row
        List<InlineKeyboardButton> quickPickRow = new ArrayList<>();
        quickPickRow.add(createButton("Сегодня", Callback.DATE + LocalDate.now()));
        quickPickRow.add(createButton("Завтра", Callback.DATE + LocalDate.now().plusDays(1)));
        quickPickRow.add(createButton("Послезавтра", Callback.DATE + LocalDate.now().plusDays(2)));

        // create keyboard
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.addAll(calendarRows);
        keyboard.add(quickPickRow);
        keyboard.add(List.of(BACK_BUTTON));

        return new InlineKeyboardMarkup(keyboard);
    }

//    public static InlineKeyboardMarkup withClock(LocalTime targetTime) {
//        // first row
//        List<InlineKeyboardButton> row1 = new ArrayList<>();
//        row1.add(createButton("🔺", Callback.NEXT_HOUR + targetTime.plusHours(1L).format(DateTimeFormatter.ofPattern("HH:mm"))));
//        row1.add(createButton("🔺", Callback.NEXT_MINUTE + targetTime.plusMinutes(1L).format(DateTimeFormatter.ofPattern("HH:mm"))));
//
//        // second row
//        List<InlineKeyboardButton> row2 = new ArrayList<>();
//        row2.add(createButton(targetTime.format(DateTimeFormatter.ofPattern("HH")), Callback.EMPTY));
//        row2.add(createButton(targetTime.format(DateTimeFormatter.ofPattern("mm")), Callback.EMPTY));
//
//        // third row
//        List<InlineKeyboardButton> row3 = new ArrayList<>();
//        row3.add(createButton("🔻", Callback.PREVIOUS_HOUR + targetTime.minusHours(1L).format(DateTimeFormatter.ofPattern("HH:mm"))));
//        row3.add(createButton("🔻", Callback.PREVIOUS_MINUTE + targetTime.minusMinutes(1L).format(DateTimeFormatter.ofPattern("HH:mm"))));
//
//        // create keyboard
//        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
//        keyboard.add(row1);
//        keyboard.add(row2);
//        keyboard.add(row3);
//        keyboard.add(List.of(createButton("✅ Подтвердить", Callback.TIME + targetTime.format(DateTimeFormatter.ofPattern("HH:mm")))));
//        keyboard.add(List.of(BACK_BUTTON));
//
//        return new InlineKeyboardMarkup(keyboard);
//    }

//    public static ReplyKeyboard withClock2(LocalTime targetTime) {
//        KeyboardRow row1 = new KeyboardRow();
//        row1.add(new KeyboardButton("1"));
//        row1.add(new KeyboardButton("2"));
//        row1.add(new KeyboardButton("3"));
//
//        KeyboardRow row2 = new KeyboardRow();
//        row2.add(new KeyboardButton("4"));
//        row2.add(new KeyboardButton("5"));
//        row2.add(new KeyboardButton("6"));
//
//        KeyboardRow row3 = new KeyboardRow();
//        row3.add(new KeyboardButton("7"));
//        row3.add(new KeyboardButton("8"));
//        row3.add(new KeyboardButton("9"));
//
//        return new ReplyKeyboardMarkup(List.of(row1, row2, row3));
//    }

    public static InlineKeyboardMarkup withClock(String[] targetTime) {
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        row1.add(createButton(convertToEmoji(targetTime[0]) + convertToEmoji(targetTime[1]) + ":"
                + convertToEmoji(targetTime[2]) + convertToEmoji(targetTime[3]), Callback.EMPTY));

        List<InlineKeyboardButton> row2 = new ArrayList<>();
        row2.add(createButton("1", Callback.CHANGE_TIME + 1));
        row2.add(createButton("2", Callback.CHANGE_TIME + 2));
        row2.add(createButton("3", Callback.CHANGE_TIME + 3));

        List<InlineKeyboardButton> row3 = new ArrayList<>();
        row3.add(createButton("4", Callback.CHANGE_TIME + 4));
        row3.add(createButton("5", Callback.CHANGE_TIME + 5));
        row3.add(createButton("6", Callback.CHANGE_TIME + 6));

        List<InlineKeyboardButton> row4 = new ArrayList<>();
        row4.add(createButton("7", Callback.CHANGE_TIME + 7));
        row4.add(createButton("8", Callback.CHANGE_TIME + 8));
        row4.add(createButton("9", Callback.CHANGE_TIME + 9));

        List<InlineKeyboardButton> row5 = new ArrayList<>();
        row5.add(createButton("0", Callback.CHANGE_TIME + 0));
        row5.add(createButton("◀\uFE0F", Callback.CHANGE_TIME_BACKSPACE));

//        String time = Arrays.stream(targetTime).map(KeyboardFactory::convertToEmoji).collect(Collectors.joining(""));

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);
        keyboard.add(row4);
        keyboard.add(row5);
        keyboard.add(List.of(createButton("✅ Подтвердить", Callback.TIME + String.join("", targetTime))));
        keyboard.add(List.of(BACK_BUTTON));

        return new InlineKeyboardMarkup(keyboard);
    }

    @NotNull
    private static String convertToEmoji(String digit) {
        return switch (digit) {
            case "0" -> "0\uFE0F⃣";
            case "1" -> "1\uFE0F⃣";
            case "2" -> "2\uFE0F⃣";
            case "3" -> "3\uFE0F⃣";
            case "4" -> "4\uFE0F⃣";
            case "5" -> "5\uFE0F⃣";
            case "6" -> "6\uFE0F⃣";
            case "7" -> "7\uFE0F⃣";
            case "8" -> "8\uFE0F⃣";
            case "9" -> "9\uFE0F⃣";
            case "x" -> "\u274C";
            default -> "-1";
        };
    }

    private static InlineKeyboardButton createButton(String text, String callback) {
        InlineKeyboardButton button = new InlineKeyboardButton(text);
        button.setCallbackData(callback);
        return button;
    }
}
