package ru.mdimension.stand_bot.command.menu.action;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.mdimension.stand_bot.command.Command;
import ru.mdimension.stand_bot.config.InlineKeyboardBuilder;
import ru.mdimension.stand_bot.dto.ShotUpdateDto;

import static ru.mdimension.stand_bot.ExampleBotApplication.dev1;
import static ru.mdimension.stand_bot.ExampleBotApplication.dev2;
import static ru.mdimension.stand_bot.ExampleBotApplication.dev3;
import static ru.mdimension.stand_bot.ExampleBotApplication.dev4;
import static ru.mdimension.stand_bot.ExampleBotApplication.dev5;
import static ru.mdimension.stand_bot.ExampleBotApplication.prerelease;
import static ru.mdimension.stand_bot.ExampleBotApplication.stable;
import static ru.mdimension.stand_bot.ExampleBotApplication.test1;
import static ru.mdimension.stand_bot.ExampleBotApplication.test2;
import static ru.mdimension.stand_bot.Util.getStatusText;
import static ru.mdimension.stand_bot.constant.BotConstant.START;

public class Restart implements Command {
    private ShotUpdateDto updateDto;

    @Override
    public SendMessage apply(long chatId) {
        return InlineKeyboardBuilder.create(chatId)
                .setText("Привет, "
                        + updateDto.getCurrentUserFirstName() + ", рад снова тебя видеть!")
                .row()
                .button(getStatusText(dev1.getNameTitle()), dev1.INFO_COMMAND)
                .endRow()
                .row()
                .button(getStatusText(dev2.getNameTitle()), dev2.INFO_COMMAND)
                .endRow()
                .row()
                .button(getStatusText(dev3.getNameTitle()), dev3.INFO_COMMAND)
                .endRow()
                .row()
                .button(getStatusText(dev4.getNameTitle()), dev4.INFO_COMMAND)
                .endRow()
                .row()
                .button(getStatusText(dev5.getNameTitle()), dev5.INFO_COMMAND)
                .endRow()
                .row()
                .button(getStatusText(test1.getNameTitle()), test1.INFO_COMMAND)
                .endRow()
                .row()
                .button(getStatusText(test2.getNameTitle()), test2.INFO_COMMAND)
                .endRow()
                .row()
                .button(getStatusText(prerelease.getNameTitle()), prerelease.INFO_COMMAND)
                .endRow()
                .row()
                .button(getStatusText(stable.getNameTitle()), stable.INFO_COMMAND)
                .endRow()
                .row()
                .button("Обновить", START)
                .endRow()
                .build();
    }

    public Restart(ShotUpdateDto dto) {
        this.updateDto = dto;
    }
}
