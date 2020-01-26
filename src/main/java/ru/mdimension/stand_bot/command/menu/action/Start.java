package ru.mdimension.stand_bot.command.menu.action;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.mdimension.stand_bot.command.Command;
import ru.mdimension.stand_bot.config.InlineKeyboardBuilder;
import ru.mdimension.stand_bot.domain.CustomTimer;
import ru.mdimension.stand_bot.dto.ShotUpdateDto;

import static ru.mdimension.stand_bot.constant.BotConstant.START;

public class Start implements Command {
    private ShotUpdateDto updateDto;
    private CustomTimer customTimer;

    @Override
    public SendMessage apply(long chatId) {
        customTimer.start(updateDto);
        return InlineKeyboardBuilder.create(chatId)
                .setText("Готово!")
                .row()
                .button("Назад", START)
                .endRow()
                .build();
    }

    public Start(ShotUpdateDto dto, CustomTimer customTimer) {
        this.updateDto = dto;
        this.customTimer = customTimer;
    }
}
