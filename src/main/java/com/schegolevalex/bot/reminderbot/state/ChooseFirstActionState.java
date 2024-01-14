package com.schegolevalex.bot.reminderbot.state;

import com.schegolevalex.bot.reminderbot.Constant;
import com.schegolevalex.bot.reminderbot.KeyboardFactory;
import com.schegolevalex.bot.reminderbot.ReminderBot;
import com.schegolevalex.bot.reminderbot.handler.Handler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.util.AbilityUtils;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;

@Component
public class ChooseFirstActionState extends AbstractState {
    private final WatchRemindersState watchRemindersState;
    private final AddReminderTextState addReminderTextState;
    private final UpdateReminderState updateReminderState;
    private final DeleteReminderState deleteReminderState;
    private final WrongInputCommonState wrongInputCommonState;

    @Autowired
    public ChooseFirstActionState(Map<String, Handler> handlerMap,
                                  ReminderBot bot,
                                  WatchRemindersState watchRemindersState,
                                  AddReminderTextState addReminderTextState,
                                  UpdateReminderState updateReminderState,
                                  DeleteReminderState deleteReminderState,
                                  WrongInputCommonState wrongInputCommonState) {
        super(handlerMap, bot);
        this.watchRemindersState = watchRemindersState;
        this.addReminderTextState = addReminderTextState;
        this.updateReminderState = updateReminderState;
        this.deleteReminderState = deleteReminderState;
        this.wrongInputCommonState = wrongInputCommonState;
    }

    @Override
    public void handle(Update update) {
        Long chatId = AbilityUtils.getChatId(update);
        try {
            switch (update.getCallbackQuery().getData()) {
                case (Constant.GO_TO_MY_REMINDERS) -> getBot().pushBotState(chatId, watchRemindersState);
                case (Constant.GO_TO_ADD_REMINDER) -> getBot().pushBotState(chatId, addReminderTextState);
                case (Constant.GO_TO_UPDATE_REMINDER) -> getBot().pushBotState(chatId, updateReminderState);
                case (Constant.GO_TO_DELETE_REMINDER) -> getBot().pushBotState(chatId, deleteReminderState);
                case (Constant.GO_BACK) -> getBot().popBotState(chatId);
                default -> getBot().pushBotState(chatId, wrongInputCommonState);
            }
        } catch (Exception e) {
            getBot().pushBotState(chatId, wrongInputCommonState);
        }
    }

    @Override
    public BotApiMethod<?> reply(Update update) {
        return EditMessageText.builder()
                .chatId(AbilityUtils.getChatId(update))
                .messageId(update.getMessage().getMessageId())
                .text(Constant.CHOOSE_FIRST_ACTION_DESCRIPTION)
                .replyMarkup(KeyboardFactory.withFirstActionMessage())
                .build();
    }
}