package com.schegolevalex.bot.reminderbot.handler;

import com.schegolevalex.bot.reminderbot.Constant;
import com.schegolevalex.bot.reminderbot.ReminderBot;
import com.schegolevalex.bot.reminderbot.state.AbstractState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.util.AbilityUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;

@Component
public class CallbackHandler extends Handler {

    @Autowired
    protected CallbackHandler(Map<String, AbstractState> stateMap) {
        super(stateMap);
    }

    @Override
    public void handle(Update update, ReminderBot bot) {
//        Long chatId = AbilityUtils.getChatId(update);
//        switch (update.getCallbackQuery().getData()) {
//            case (Constant.GO_TO_MY_REMINDERS) -> bot.pushBotState(chatId, getStateMap().get("watchRemindersState"));
//            case (Constant.GO_TO_ADD_REMINDER) -> bot.pushBotState(chatId, getStateMap().get("addReminderTextState"));
//            case (Constant.GO_TO_UPDATE_REMINDER) -> bot.pushBotState(chatId, getStateMap().get("updateReminderState"));
//            case (Constant.GO_TO_DELETE_REMINDER) -> bot.pushBotState(chatId, getStateMap().get("deleteReminderState"));
//            case (Constant.GO_BACK) -> bot.popBotState(chatId);
//            default -> bot.pushBotState(chatId, getStateMap().get("wrongInputState"));
//        }
    }
}
