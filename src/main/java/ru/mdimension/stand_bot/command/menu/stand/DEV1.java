package ru.mdimension.stand_bot.command.menu.stand;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.mdimension.stand_bot.command.Command;

import static ru.mdimension.stand_bot.ExampleBotApplication.dev1;
import static ru.mdimension.stand_bot.Util.getSendMessage;
import static ru.mdimension.stand_bot.constant.BotConstant.DEV1_COMMAND_START;
import static ru.mdimension.stand_bot.constant.BotConstant.DEV1_COMMAND_STOP;
import static ru.mdimension.stand_bot.constant.BotConstant.DEV1_NAME;

public class DEV1 implements Command {
    @Override
    public SendMessage apply(long chatId) {
        return getSendMessage(chatId, DEV1_NAME, dev1, DEV1_COMMAND_START, DEV1_COMMAND_STOP);
    }


}

