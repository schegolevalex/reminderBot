package com.schegolevalex.bot.reminderbot.state;

import com.schegolevalex.bot.reminderbot.ReminderBot;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.util.AbilityUtils;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class WrongInputDateState extends AbstractState {

    public WrongInputDateState(@Lazy ReminderBot bot) {
        super(bot);
    }

    @Override
    public BotApiMethod<?> reply(Update update) {
        return null;
//        EditMessageText editMessageText = new EditMessageText();
//        editMessageText.setChatId(String.valueOf(chatId));
//        editMessageText.setText(Constant.WRONG_DATE_FORMAT);
//        editMessageText.setMessageId(messageIds.get(String.valueOf(chatId)));
//        try {
//            bot.execute(editMessageText);
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void perform(Update update) {
        bot.pushBotState(AbilityUtils.getChatId(update), State.ADD_REMINDER_DATE);
    }

    @Override
    public State getType() {
        return State.WRONG_INPUT_DATE;
    }
}
