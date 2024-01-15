package com.schegolevalex.bot.reminderbot.state;

import com.schegolevalex.bot.reminderbot.Constant;
import com.schegolevalex.bot.reminderbot.KeyboardFactory;
import com.schegolevalex.bot.reminderbot.ReminderBot;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.util.AbilityUtils;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class ChooseFirstActionState extends AbstractState {

    public ChooseFirstActionState(@Lazy ReminderBot bot) {
        super(bot);
    }

    @Override
    public void handle(Update update) {
        Long chatId = AbilityUtils.getChatId(update);
        try {
            switch (update.getCallbackQuery().getData()) {
                case (Constant.GO_TO_MY_REMINDERS) ->
                        getBot().pushBotState(chatId, getBot().findStateByType(State.WATCH_REMINDERS));
                case (Constant.GO_TO_ADD_REMINDER) ->
                        getBot().pushBotState(chatId, getBot().findStateByType(State.ADD_REMINDER_TEXT));
                case (Constant.GO_TO_UPDATE_REMINDER) ->
                        getBot().pushBotState(chatId, getBot().findStateByType(State.UPDATE_REMINDER));
                case (Constant.GO_TO_DELETE_REMINDER) ->
                        getBot().pushBotState(chatId, getBot().findStateByType(State.DELETE_REMINDER));
                case (Constant.GO_BACK) -> getBot().popBotState(chatId);
                default -> getBot().pushBotState(chatId, getBot().findStateByType(State.WRONG_INPUT_COMMON));
            }
        } catch (Exception e) {
            getBot().pushBotState(chatId, getBot().findStateByType(State.WRONG_INPUT_COMMON));
        }
    }

    @Override
    public BotApiMethod<?> reply(Update update) {
        return SendMessage.builder()
                .chatId(AbilityUtils.getChatId(update))
//                .messageId(update.getMessage().getMessageId())
                .text(Constant.CHOOSE_FIRST_ACTION_DESCRIPTION)
                .replyMarkup(KeyboardFactory.withFirstActionMessage())
                .build();
    }

    @Override
    public State getType() {
        return State.CHOOSE_FIRST_ACTION;
    }
}