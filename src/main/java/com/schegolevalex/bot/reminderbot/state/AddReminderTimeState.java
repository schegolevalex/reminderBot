package com.schegolevalex.bot.reminderbot.state;

import com.schegolevalex.bot.reminderbot.ChatContext;
import com.schegolevalex.bot.reminderbot.CustomReply;
import com.schegolevalex.bot.reminderbot.KeyboardFactory;
import com.schegolevalex.bot.reminderbot.ReminderBot;
import com.schegolevalex.bot.reminderbot.entity.Reminder;
import com.schegolevalex.bot.reminderbot.service.ReminderService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.util.AbilityUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static com.schegolevalex.bot.reminderbot.config.Constant.Callback;
import static com.schegolevalex.bot.reminderbot.config.Constant.Message;

@Component
public class AddReminderTimeState extends AbstractState {
    private final ReminderService reminderService;

    public AddReminderTimeState(@Lazy ReminderBot bot,
                                ReminderService reminderService) {
        super(bot);
        this.reminderService = reminderService;
    }

    @Override
    public CustomReply reply(Update update) {
        Long chatId = AbilityUtils.getChatId(update);
        ChatContext chatContext = bot.getChatContext(chatId);
        String reminderText = chatContext.getTempReminder().getText();
        String date = chatContext.getTempReminder().getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        String[] targetTime = chatContext.getTargetTime();

        if (update.hasCallbackQuery()) {
            String data = update.getCallbackQuery().getData();
            int cursor = chatContext.getTimeInputCursor();

            if (data.startsWith(Callback.CHANGE_TIME) && cursor < 4) {
                targetTime[cursor] = data.substring(data.length() - 1);
                chatContext.setTargetTime(targetTime);
                chatContext.setTimeInputCursor(++cursor);
            }

            if (data.equals(Callback.CHANGE_TIME_BACKSPACE) && cursor > 0) {
                targetTime[cursor - 1] = "x";
                chatContext.setTargetTime(targetTime);
                chatContext.setTimeInputCursor(--cursor);
            }
        }

        return CustomReply.builder()
                .text("Текст напоминания: \"" + reminderText + "\"\n" +
                        "Дата напоминания: " + date + "\"\n" +
                        Message.ADD_REMINDER_TIME_DESCRIPTION)
                .replyMarkup(KeyboardFactory.withClock(targetTime))
                .build();
//        Long chatId = AbilityUtils.getChatId(update);
//        String reminderText = bot.getChatContext(chatId).getTempReminder().getText();
//        String date = bot.getChatContext(chatId).getTempReminder().getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
//        LocalTime targetTime = LocalTime.now();
//
//        if (update.hasCallbackQuery()) {
//            String data = update.getCallbackQuery().getData();
//            if (data.startsWith(Callback.PREVIOUS_HOUR) || data.startsWith(Callback.NEXT_HOUR) || data.startsWith(Callback.PREVIOUS_MINUTE) || data.startsWith(Callback.NEXT_MINUTE))
//                targetTime = LocalTime.parse(data.substring(data.length() - 5), DateTimeFormatter.ofPattern("HH:mm"));
//        }
//
//        return CustomReply.builder()
//                .text("Текст напоминания: \"" + reminderText + "\"\n" +
//                        "Дата напоминания: " + date + "\"\n" +
//                        Message.ADD_REMINDER_TIME_DESCRIPTION)
//                .replyMarkup(KeyboardFactory.withClock(targetTime))
//                .build();
    }

    @Override
    public void perform(Update update) {
        Long chatId = AbilityUtils.getChatId(update);

        if (update.hasCallbackQuery()) {
            String data = update.getCallbackQuery().getData();
            Reminder tempReminder = bot.getChatContext(chatId).getTempReminder();

            if (data.equals(Callback.GO_BACK))
                bot.popState(chatId);
            else if (data.startsWith(Callback.TIME)) {
                try {
                    tempReminder.setTime(LocalTime.parse(data.substring(data.length() - 4), DateTimeFormatter.ofPattern("HHmm")));
                    reminderService.saveReminder(tempReminder);
                    bot.pushState(chatId, State.SUCCESSFUL_ADDITION);
                    bot.getChatContext(chatId).setTargetTime(new String[]{"x", "x", "x", "x"});
                    bot.getChatContext(chatId).setTimeInputCursor(0);
                    bot.remind(tempReminder);
                } catch (DateTimeParseException e) {
                    bot.pushState(chatId, State.WRONG_INPUT_TIME);
                }
            }
        } else
            bot.pushState(chatId, State.WRONG_INPUT);

        // вариант с обработкой ввода текста с клавиатуры
//        Long chatId = AbilityUtils.getChatId(update);
//
//        if (update.hasMessage() && update.getMessage().hasText()) {
//            Reminder tempReminder = bot.getChatContext(chatId).getTempReminder();
//            String text = update.getMessage().getText();
//            LocalTime time;
//            try {
//                time = LocalTime.parse(text);
//            } catch (DateTimeParseException e) {
//                bot.pushState(chatId, State.WRONG_INPUT_TIME);
//                return;
//            }
//            tempReminder.setTime(time);
//
//            reminderService.saveReminder(tempReminder);
//            bot.remind(tempReminder);
//            bot.pushState(chatId, State.SUCCESSFUL_ADDITION);
//        } else if (update.hasCallbackQuery() && update.getCallbackQuery().getData().equals(Callback.GO_BACK))
//            bot.popState(chatId);
//        else
//            bot.pushState(chatId, State.WRONG_INPUT_DATE);
    }

    @Override
    public State getType() {
        return State.ADD_REMINDER_TIME;
    }
}
