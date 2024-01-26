package com.schegolevalex.bot.reminderbot.state;

import com.schegolevalex.bot.reminderbot.ChatContext;
import com.schegolevalex.bot.reminderbot.CustomReply;
import com.schegolevalex.bot.reminderbot.KeyboardFactory;
import com.schegolevalex.bot.reminderbot.ReminderBot;
import com.schegolevalex.bot.reminderbot.config.Constant;
import com.schegolevalex.bot.reminderbot.entity.Reminder;
import com.schegolevalex.bot.reminderbot.service.ReminderService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.util.AbilityUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static com.schegolevalex.bot.reminderbot.config.Constant.Message;

@Component
public class EditReminderTimeState extends AbstractState {
    private final ReminderService reminderService;

    public EditReminderTimeState(@Lazy ReminderBot bot, ReminderService reminderService) {
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

            if (data.startsWith(Constant.Callback.CHANGE_TIME) && cursor < 4) {
                targetTime[cursor] = data.substring(data.length() - 1);
                chatContext.setTargetTime(targetTime);
                chatContext.setTimeInputCursor(++cursor);
            }

            if (data.equals(Constant.Callback.CHANGE_TIME_BACKSPACE) && cursor > 0) {
                targetTime[cursor - 1] = "x";
                chatContext.setTargetTime(targetTime);
                chatContext.setTimeInputCursor(--cursor);
            }
        }

        return CustomReply.builder()
                .text(String.format(Message.EDIT_REMINDER_TIME_DESCRIPTION, chatContext.getTempReminder().getTime()))
                .replyMarkup(KeyboardFactory.withClock(targetTime))
                .build();

//        String data = update.getCallbackQuery().getData();
//        String id = data.substring(data.length() - 36);
//        Optional<Reminder> mayBeReminder = reminderService.getReminderById(UUID.fromString(id));
//
//        if (mayBeReminder.isPresent()) {
//            Reminder reminder = mayBeReminder.get();
//            bot.getChatContext(AbilityUtils.getChatId(update)).setTempReminder(reminder);
//            return CustomReply.builder()
//                    .text(String.format(Message.EDIT_REMINDER_TIME_DESCRIPTION, reminder.getTime()))
//                    .replyMarkup(KeyboardFactory.withBackButton())
//                    .build();
//        } else
//            return CustomReply.builder()
//                    .text(Message.UNKNOWN_REMINDER)
//                    .replyMarkup(KeyboardFactory.withBackButton())
//                    .build();
    }

    @Override
    public void perform(Update update) {
        Long chatId = AbilityUtils.getChatId(update);

        if (update.hasCallbackQuery()) {
            String data = update.getCallbackQuery().getData();
            Reminder tempReminder = bot.getChatContext(chatId).getTempReminder();

            if (data.equals(Constant.Callback.GO_BACK))
                bot.popState(chatId);
            else if (data.startsWith(Constant.Callback.TIME)) {
                try {
                    tempReminder.setTime(LocalTime.parse(data.substring(data.length() - 4), DateTimeFormatter.ofPattern("HHmm")));
                    reminderService.saveReminder(tempReminder);
                    bot.pushState(chatId, State.SUCCESSFUL_EDITING);
                    bot.getChatContext(chatId).setTargetTime(new String[]{"x", "x", "x", "x"});
                    bot.getChatContext(chatId).setTimeInputCursor(0);
                    bot.remind(tempReminder);
                } catch (DateTimeParseException e) {
                    bot.pushState(chatId, State.WRONG_INPUT_TIME);
                }
            }
        } else
            bot.pushState(chatId, State.WRONG_INPUT);
    }


//        Long chatId = AbilityUtils.getChatId(update);
//        if (update.hasMessage() && update.getMessage().hasText()) {
//            LocalTime newTime;
//            try {
//                newTime = LocalTime.parse(update.getMessage().getText());
//            } catch (DateTimeParseException e) {
//                bot.pushState(chatId, State.WRONG_INPUT_TIME);
//                return;
//            }
//            Reminder editedReminder = bot.getChatContext(chatId).getTempReminder();
//            editedReminder.setTime(newTime);
//            reminderService.saveReminder(editedReminder);
//            bot.remind(editedReminder);
//            bot.pushState(chatId, State.SUCCESSFUL_EDITING);
//        } else if (update.hasCallbackQuery() && update.getCallbackQuery().getData().equals(Constant.Callback.GO_BACK))
//            bot.popState(chatId);
//        else
//            bot.pushState(chatId, State.WRONG_INPUT);
//    }

    @Override
    public State getType() {
        return State.EDIT_REMINDER_TIME;
    }
}
