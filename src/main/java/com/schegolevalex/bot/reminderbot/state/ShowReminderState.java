package com.schegolevalex.bot.reminderbot.state;

import com.schegolevalex.bot.reminderbot.CustomReply;
import com.schegolevalex.bot.reminderbot.ReminderBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.util.AbilityUtils;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static com.schegolevalex.bot.reminderbot.config.Constant.Callback;

@Component
@Slf4j
public class ShowReminderState extends AbstractState {
    public ShowReminderState(@Lazy ReminderBot bot) {
        super(bot);
    }

    @Override
    public CustomReply reply(Update update) {
        return null;
    }

    @Override
    public void perform(Update update) {
        Long chatId = AbilityUtils.getChatId(update);

        if (update.hasCallbackQuery() && update.getCallbackQuery().getData().equals(Callback.OK)) {
            bot.popState(chatId);
            Integer idToDelete = bot.getChatContext(chatId).getMessageIdToDelete();
            if (idToDelete != null) {
                try {
                    bot.execute(DeleteMessage.builder()
                            .chatId(chatId)
                            .messageId(idToDelete)
                            .build());
                } catch (TelegramApiException e) {
                    log.error("*********** Не удалось отправить сообщение ************"); //todo
                }
            }
        } else
            bot.pushState(chatId, State.WRONG_INPUT);
    }

    @Override
    public State getType() {
        return State.SHOW_REMINDER;
    }
}
