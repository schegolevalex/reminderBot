package com.schegolevalex.bot.reminderbot.state;

import com.schegolevalex.bot.reminderbot.ReminderBot;
import com.schegolevalex.bot.reminderbot.handler.Handler;
import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;

@Getter
@Setter
public abstract class AbstractState {

    private final Map<String, Handler> handlerMap;
    private final ReminderBot bot;

    public AbstractState(Map<String, Handler> handlerMap, ReminderBot bot) {
        this.handlerMap = handlerMap;
        this.bot = bot;
    }

    public abstract void handle(Update update); /*{
        Long chatId = AbilityUtils.getChatId(update);
        AbstractState state = getBot().getBotState(chatId);

        if (update.hasMessage() && update.getMessage().isCommand()) {
            new CallbackHandler().
        }


        if (state instanceof WrongInputDateState) {
            getBot().popBotState(chatId);
            getHandlerMap().get("wrongInputDateHandler").handle(update, getBot());
        } else if (state instanceof WrongInputTimeState) {
            getBot().popBotState(chatId);
            getHandlerMap().get("wrongInputTimeHandler").handle(update, getBot());
        } else if (update.hasCallbackQuery())
            getHandlerMap().get("callbackHandler").handle(update, getBot());
        else if (update.hasMessage() && update.getMessage().isCommand())
            getHandlerMap().get("commandHandler").handle(update, getBot());
        else if (update.hasMessage() && update.getMessage().hasText())
            getHandlerMap().get("textHandler").handle(update, getBot());
        else getHandlerMap().get("wrongInputCommonHandler").handle(update, getBot());
    }*/

    public abstract BotApiMethod<?> reply(Update update);
}