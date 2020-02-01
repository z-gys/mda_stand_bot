package ru.mdimension.stand_bot.command.menu.action;

import lombok.EqualsAndHashCode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.mdimension.stand_bot.command.Command;
import ru.mdimension.stand_bot.config.InlineKeyboardBuilder;
import ru.mdimension.stand_bot.domain.CustomTimer;

import static ru.mdimension.stand_bot.constant.BotConstant.START;

@EqualsAndHashCode
public class Stop implements Command {
    private CustomTimer customTimer;

    @Override
    public SendMessage apply(long chatId) {
        customTimer.stop();
        return InlineKeyboardBuilder.create(chatId)
                .setText("Готово!")
                .row()
                .button("Назад", START)
                .endRow()
                .build();
    }

    public Stop(CustomTimer customTimer) {
        this.customTimer = customTimer;
    }
}
