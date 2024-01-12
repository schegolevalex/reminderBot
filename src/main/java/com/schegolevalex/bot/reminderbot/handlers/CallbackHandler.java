package com.schegolevalex.bot.reminderbot.handlers;

import com.schegolevalex.bot.reminderbot.Constant;
import com.schegolevalex.bot.reminderbot.repliers.AbstractReplier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Stack;

@Component
public class CallbackHandler extends Handler {

    @Override
    public void handle(Update update, Stack<AbstractReplier> replierStack) {
        switch (update.getCallbackQuery().getData()) {
            case (Constant.GO_TO_MY_REMINDERS) -> replierStack.push(replierMap.get("watchRemindersReplier"));
            case (Constant.GO_TO_ADD_REMINDER) -> replierStack.push(replierMap.get("addReminderTextReplier"));
            case (Constant.GO_TO_UPDATE_REMINDER) -> replierStack.push(replierMap.get("updateReminderReplier"));
            case (Constant.GO_TO_DELETE_REMINDER) -> replierStack.push(replierMap.get("deleteReminderReplier"));
            case (Constant.GO_BACK) -> replierStack.pop();
            default -> replierStack.push(replierMap.get("wrongInputReplier"));
        }
    }
}
