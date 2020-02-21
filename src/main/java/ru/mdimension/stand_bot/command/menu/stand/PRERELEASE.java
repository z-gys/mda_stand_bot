package ru.mdimension.stand_bot.command.menu.stand;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.mdimension.stand_bot.command.Command;
import ru.mdimension.stand_bot.dto.ShotUpdateDto;

import static ru.mdimension.stand_bot.ExampleBotApplication.prerelease;
import static ru.mdimension.stand_bot.Util.getSendMessage;

public class PRERELEASE implements Command {
    private ShotUpdateDto updateDto;

    @Override
    public SendMessage apply(long chatId) {
        return getSendMessage(chatId, updateDto, prerelease);
    }

    public PRERELEASE(ShotUpdateDto dto) {
        this.updateDto = dto;
    }
}

