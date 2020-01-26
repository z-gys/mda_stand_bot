package ru.mdimension.stand_bot.command.menu.stand;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.mdimension.stand_bot.command.Command;

import static ru.mdimension.stand_bot.ExampleBotApplication.dev2;
import static ru.mdimension.stand_bot.Util.getSendMessage;
import static ru.mdimension.stand_bot.constant.BotConstant.DEV2_COMMAND_START;
import static ru.mdimension.stand_bot.constant.BotConstant.DEV2_COMMAND_STOP;
import static ru.mdimension.stand_bot.constant.BotConstant.DEV2_NAME;

public class DEV2 implements Command {
    @Override
    public SendMessage apply(long chatId) {
        return getSendMessage(chatId, DEV2_NAME, dev2, DEV2_COMMAND_START, DEV2_COMMAND_STOP);
    }
}

