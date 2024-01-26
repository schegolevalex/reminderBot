package com.schegolevalex.bot.reminderbot.state;

import com.schegolevalex.bot.reminderbot.CustomReply;
import com.schegolevalex.bot.reminderbot.KeyboardFactory;
import com.schegolevalex.bot.reminderbot.ReminderBot;
import com.schegolevalex.bot.reminderbot.entity.Reminder;
import com.schegolevalex.bot.reminderbot.service.ReminderService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.util.AbilityUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.schegolevalex.bot.reminderbot.config.Constant.Callback;
import static com.schegolevalex.bot.reminderbot.config.Constant.Message;

@Component
public class EditReminderTextState extends AbstractState {
    private final ReminderService reminderService;

    public EditReminderTextState(@Lazy ReminderBot bot, ReminderService reminderService) {
        super(bot);
        this.reminderService = reminderService;
    }

    @Override
    public CustomReply reply(Update update) {
        Reminder editedReminder = bot.getChatContext(AbilityUtils.getChatId(update)).getTempReminder();
        return CustomReply.builder()
                .text(String.format(Message.EDIT_REMINDER_TEXT_DESCRIPTION, editedReminder.getText()))
                .replyMarkup(KeyboardFactory.withBackButton())
                .build();
    }

    @Override
    public void perform(Update update) {
        Long chatId = AbilityUtils.getChatId(update);
        if (update.hasMessage() && update.getMessage().hasText()) {
            String newText = update.getMessage().getText();
            Reminder editedReminder = bot.getChatContext(chatId).getTempReminder();
            editedReminder.setText(newText);
            reminderService.saveReminder(editedReminder);
            bot.pushState(chatId, State.SUCCESSFUL_EDITING);
        } else if (update.hasCallbackQuery() && update.getCallbackQuery().getData().equals(Callback.GO_BACK))
            bot.popState(chatId);
        else
            bot.pushState(chatId, State.WRONG_INPUT);
    }

    @Override
    public State getType() {
        return State.EDIT_REMINDER_TEXT;
    }
}
