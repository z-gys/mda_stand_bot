package ru.mdimension.stand_bot.command.menu.stand;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.mdimension.stand_bot.command.Command;

import static ru.mdimension.stand_bot.ExampleBotApplication.test2;
import static ru.mdimension.stand_bot.Util.getSendMessage;
import static ru.mdimension.stand_bot.constant.BotConstant.TEST2_COMMAND_START;
import static ru.mdimension.stand_bot.constant.BotConstant.TEST2_COMMAND_STOP;
import static ru.mdimension.stand_bot.constant.BotConstant.TEST2_NAME;

public class Test2 implements Command {
    @Override
    public SendMessage apply(long chatId) {
        return getSendMessage(chatId, TEST2_NAME, test2, TEST2_COMMAND_START, TEST2_COMMAND_STOP);
    }
}

