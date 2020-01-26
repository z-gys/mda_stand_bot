package ru.mdimension.stand_bot.command.menu;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.mdimension.stand_bot.command.Command;
import ru.mdimension.stand_bot.config.InlineKeyboardBuilder;
import ru.mdimension.stand_bot.dto.ShotUpdateDto;

import static ru.mdimension.stand_bot.ExampleBot.getStatusText;
import static ru.mdimension.stand_bot.constant.BotConstant.DEV1_COMMAND;
import static ru.mdimension.stand_bot.constant.BotConstant.DEV1_NAME;
import static ru.mdimension.stand_bot.constant.BotConstant.DEV2_COMMAND;
import static ru.mdimension.stand_bot.constant.BotConstant.DEV2_NAME;
import static ru.mdimension.stand_bot.constant.BotConstant.TEST1_COMMAND;
import static ru.mdimension.stand_bot.constant.BotConstant.TEST1_NAME;
import static ru.mdimension.stand_bot.constant.BotConstant.TEST2_COMMAND;
import static ru.mdimension.stand_bot.constant.BotConstant.TEST2_NAME;

public class MainMenu implements Command {
    private ShotUpdateDto updateDto;

    @Override
    public SendMessage apply(long chatId) {
        return InlineKeyboardBuilder.create(chatId)
                .setText("Привет, "
                        + updateDto.getCurrentUserFirstName() + " рад снова тебя видеть!")
                .row()
                .button(getStatusText(DEV1_NAME), DEV1_COMMAND)
                .endRow()
                .row()
                .button(getStatusText(DEV2_NAME), DEV2_COMMAND)
                .endRow()
                .row()
                .button(getStatusText(TEST1_NAME), TEST1_COMMAND)
                .endRow()
                .row()
                .button(getStatusText(TEST2_NAME), TEST2_COMMAND)
                .endRow()
                .build();
    }

    public MainMenu(ShotUpdateDto dto) {
        this.updateDto = dto;
    }
}
