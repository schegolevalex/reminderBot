package com.schegolevalex.bot.reminderbot.state;

import com.schegolevalex.bot.reminderbot.Constant;
import com.schegolevalex.bot.reminderbot.ReminderBot;
import com.schegolevalex.bot.reminderbot.handler.Handler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.util.AbilityUtils;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;

@Component
public class AwaitStartState extends AbstractState {
    private final ChooseFirstActionState chooseFirstActionState;
    private final WrongInputCommonState wrongInputCommonState;

    @Autowired
    public AwaitStartState(Map<String, Handler> handlerMap,
                           ReminderBot bot,
                           ChooseFirstActionState chooseFirstActionState,
                           WrongInputCommonState wrongInputCommonState) {
        super(handlerMap, bot);
        this.chooseFirstActionState = chooseFirstActionState;
        this.wrongInputCommonState = wrongInputCommonState;
    }

    @Override
    public void handle(Update update) {
        Long chatId = AbilityUtils.getChatId(update);

        try {
            switch (update.getMessage().getText()) {
                case ("/start") -> getBot().pushBotState(chatId, chooseFirstActionState);
                // todo
                default -> getBot().pushBotState(chatId, wrongInputCommonState);
            }
        } catch (Exception e) {
            getBot().pushBotState(chatId, wrongInputCommonState);
        }
    }

    @Override
    public BotApiMethod<?> reply(Update update) {
        return SendMessage.builder()
                .chatId(AbilityUtils.getChatId(update))
                .text(Constant.AWAIT_START_DESCRIPTION)
                .build();
    }
}
