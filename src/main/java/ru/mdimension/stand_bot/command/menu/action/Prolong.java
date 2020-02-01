package ru.mdimension.stand_bot.command.menu.action;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.mdimension.stand_bot.command.Command;
import ru.mdimension.stand_bot.config.InlineKeyboardBuilder;
import ru.mdimension.stand_bot.domain.CustomTimer;

import static ru.mdimension.stand_bot.constant.BotConstant.START;

public class Prolong implements Command {
    private CustomTimer customTimer;

    @Override
    public SendMessage apply(long chatId) {
        customTimer.prolong1Hour();
        return InlineKeyboardBuilder.create(chatId)
                .setText("Готово, стенд продлен на час!")
                .row()
                .button("Назад", START)
                .endRow()
                .build();
    }

    public Prolong(CustomTimer customTimer) {
        this.customTimer = customTimer;
    }

}
