package com.schegolevalex.bot.reminderbot.state;

import com.schegolevalex.bot.reminderbot.ReminderBot;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.util.AbilityUtils;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class WrongInputTimeState extends AbstractState {

    public WrongInputTimeState(@Lazy ReminderBot bot) {
        super(bot);
    }

    @Override
    public BotApiMethod<?> reply(Update update) {
        return null;
//        EditMessageText editMessageText = new EditMessageText();
//        editMessageText.setChatId(String.valueOf(chatId));
//        editMessageText.setText(Constant.WRONG_TIME_FORMAT);
//        editMessageText.setMessageId(messageIds.get(String.valueOf(chatId)));
//        bot.execute(editMessageText);
    }

    @Override
    public void perform(Update update) {
        bot.pushBotState(AbilityUtils.getChatId(update), State.ADD_REMINDER_TIME);
    }

    @Override
    public State getType() {
        return State.WRONG_INPUT_TIME;
    }
}
