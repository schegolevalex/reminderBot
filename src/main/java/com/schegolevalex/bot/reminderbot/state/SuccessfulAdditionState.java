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

import java.time.format.DateTimeFormatter;
import java.util.Map;

@Component
public class SuccessfulAdditionState extends AbstractState {

    private final ChooseFirstActionState chooseFirstActionState;

    @Autowired
    public SuccessfulAdditionState(Map<String, Handler> handlerMap,
                                   ReminderBot bot,
                                   ChooseFirstActionState chooseFirstActionState) {
        super(handlerMap, bot);
        this.chooseFirstActionState = chooseFirstActionState;
    }

    @Override
    public void handle(Update update) {
        Long chatId = AbilityUtils.getChatId(update);
        getBot().pushBotState(chatId, chooseFirstActionState);
    }

    @Override
    public BotApiMethod<?> reply(Update update) {
        return null;
//        Long chatId = AbilityUtils.getChatId(update);
//        return EditMessageText.builder()
//                .chatId(chatId)
//                .messageId(update.getMessage().getMessageId())
//                .text("Текст: \"" + getBot().getTempReminders().get(chatId).getText() + "\"\n" +
//                        "Дата: " + getBot().getTempReminders().get(chatId).getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + "\"\n" +
//                        Constant.ADD_REMINDER_TIME_DESCRIPTION)
//                .replyMarkup(KeyboardFactory.withBackButton())
//                .build();
//
//
//        EditMessageText editMessageText = new EditMessageText();
//        editMessageText.setText(String.format(Constant.SUCCESSFUL_ADDITION,
//                tempReminders.get(chatId).getText(),
//                tempReminders.get(chatId).getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
//                tempReminders.get(chatId).getTime()) + "\n" + Constant.CHOOSE_FIRST_ACTION_DESCRIPTION);
//
//        editMessageText.setReplyMarkup(KeyboardFactory.withFirstActionMessage());
//        editMessageText.setMessageId(messageIds.get(String.valueOf(chatId)));
//
//        bot.execute(editMessageText);
    }
}