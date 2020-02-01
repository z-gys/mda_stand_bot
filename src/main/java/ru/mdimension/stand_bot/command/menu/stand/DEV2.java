package ru.mdimension.stand_bot.command.menu.stand;

import lombok.EqualsAndHashCode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.mdimension.stand_bot.command.Command;
import ru.mdimension.stand_bot.dto.ShotUpdateDto;

import static ru.mdimension.stand_bot.ExampleBotApplication.dev2;
import static ru.mdimension.stand_bot.Util.getSendMessage;
import static ru.mdimension.stand_bot.constant.BotConstant.DEV2_COMMAND_START;
import static ru.mdimension.stand_bot.constant.BotConstant.DEV2_COMMAND_STOP;

@EqualsAndHashCode
public class DEV2 implements Command {
    private ShotUpdateDto updateDto;

    @Override
    public SendMessage apply(long chatId) {
        return getSendMessage(chatId, updateDto, dev2, DEV2_COMMAND_START, DEV2_COMMAND_STOP);
    }

    public DEV2(ShotUpdateDto dto) {
        this.updateDto = dto;
    }
}

