package ru.mdimension.stand_bot.command.menu.stand;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.mdimension.stand_bot.command.Command;

import static ru.mdimension.stand_bot.ExampleBotApplication.dev3;
import static ru.mdimension.stand_bot.Util.getSendMessage;
import static ru.mdimension.stand_bot.constant.BotConstant.DEV3_COMMAND_START;
import static ru.mdimension.stand_bot.constant.BotConstant.DEV3_COMMAND_STOP;
import static ru.mdimension.stand_bot.constant.BotConstant.DEV3_NAME;

public class DEV3 implements Command {
    @Override
    public SendMessage apply(long chatId) {
        return getSendMessage(chatId, DEV3_NAME, dev3, DEV3_COMMAND_START, DEV3_COMMAND_STOP);
    }


}

