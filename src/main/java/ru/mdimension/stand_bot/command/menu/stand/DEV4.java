package ru.mdimension.stand_bot.command.menu.stand;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.mdimension.stand_bot.command.Command;
import ru.mdimension.stand_bot.dto.ShotUpdateDto;

import static ru.mdimension.stand_bot.ExampleBotApplication.dev4;
import static ru.mdimension.stand_bot.Util.getSendMessage;
import static ru.mdimension.stand_bot.constant.BotConstant.DEV4_COMMAND_START;
import static ru.mdimension.stand_bot.constant.BotConstant.DEV4_COMMAND_STOP;

public class DEV4 implements Command {
    private ShotUpdateDto updateDto;

    @Override
    public SendMessage apply(long chatId) {
        return getSendMessage(chatId, updateDto, dev4, DEV4_COMMAND_START, DEV4_COMMAND_STOP);
    }

    public DEV4(ShotUpdateDto dto) {
        this.updateDto = dto;
    }
}

