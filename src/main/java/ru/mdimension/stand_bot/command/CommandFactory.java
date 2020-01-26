package ru.mdimension.stand_bot.command;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.mdimension.stand_bot.command.menu.MainMenu;
import ru.mdimension.stand_bot.command.menu.action.Start;
import ru.mdimension.stand_bot.command.menu.action.Stop;
import ru.mdimension.stand_bot.command.menu.stand.DEV1;
import ru.mdimension.stand_bot.command.menu.stand.DEV2;
import ru.mdimension.stand_bot.command.menu.stand.Test1;
import ru.mdimension.stand_bot.command.menu.stand.Test2;
import ru.mdimension.stand_bot.dto.ShotUpdateDto;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.mdimension.stand_bot.ExampleBotApplication.dev1;
import static ru.mdimension.stand_bot.ExampleBotApplication.dev2;
import static ru.mdimension.stand_bot.ExampleBotApplication.test1;
import static ru.mdimension.stand_bot.ExampleBotApplication.test2;
import static ru.mdimension.stand_bot.constant.BotConstant.DEV1_COMMAND;
import static ru.mdimension.stand_bot.constant.BotConstant.DEV1_COMMAND_START;
import static ru.mdimension.stand_bot.constant.BotConstant.DEV1_COMMAND_STOP;
import static ru.mdimension.stand_bot.constant.BotConstant.DEV2_COMMAND;
import static ru.mdimension.stand_bot.constant.BotConstant.DEV2_COMMAND_START;
import static ru.mdimension.stand_bot.constant.BotConstant.DEV2_COMMAND_STOP;
import static ru.mdimension.stand_bot.constant.BotConstant.START;
import static ru.mdimension.stand_bot.constant.BotConstant.TEST1_COMMAND;
import static ru.mdimension.stand_bot.constant.BotConstant.TEST1_COMMAND_START;
import static ru.mdimension.stand_bot.constant.BotConstant.TEST1_COMMAND_STOP;
import static ru.mdimension.stand_bot.constant.BotConstant.TEST2_COMMAND;
import static ru.mdimension.stand_bot.constant.BotConstant.TEST2_COMMAND_START;
import static ru.mdimension.stand_bot.constant.BotConstant.TEST2_COMMAND_STOP;

@Component
@CacheConfig(cacheNames = "message")
public class CommandFactory {


    private final Map<String, Command> commands;

    protected CommandFactory() {
        commands = new HashMap<>();
    }

    public void addCommand(final String name, final Command command) {
        commands.put(name, command);
    }

    @Cacheable
    public SendMessage executeCommand(String name, long chatId, ShotUpdateDto dto) {
        if (commands.containsKey(name)) {
            commands.get(name).apply(chatId);
        }
        return commands.get(name).apply(chatId);
    }

    public void listCommands() {
        System.out.println("Enabled commands: " + commands.keySet().stream().collect(Collectors.joining(", ")));
    }

    /* Factory pattern */
    public static CommandFactory init(long chatId, ShotUpdateDto dto) {

        final CommandFactory cf = new CommandFactory();

        // commands are added here using lambdas. It is also possible to dynamically add commands without editing the code.
        cf.addCommand(START, (c) -> new MainMenu(dto).apply(chatId));
        cf.addCommand(DEV1_COMMAND, (c) -> new DEV1().apply(chatId));
        cf.addCommand(DEV1_COMMAND_START, (c) -> new Start(dto, dev1).apply(chatId));
        cf.addCommand(DEV1_COMMAND_STOP, (c) -> new Stop(dev1).apply(chatId));

        cf.addCommand(DEV2_COMMAND, (c) -> new DEV2().apply(chatId));
        cf.addCommand(DEV2_COMMAND_START, (c) -> new Start(dto, dev2).apply(chatId));
        cf.addCommand(DEV2_COMMAND_STOP, (c) -> new Stop(dev2).apply(chatId));

        cf.addCommand(TEST1_COMMAND, (c) -> new Test1().apply(chatId));
        cf.addCommand(TEST1_COMMAND_START, (c) -> new Start(dto, test1).apply(chatId));
        cf.addCommand(TEST1_COMMAND_STOP, (c) -> new Stop(test1).apply(chatId));

        cf.addCommand(TEST2_COMMAND, (c) -> new Test2().apply(chatId));
        cf.addCommand(TEST2_COMMAND_START, (c) -> new Start(dto, test2).apply(chatId));
        cf.addCommand(TEST2_COMMAND_STOP, (c) -> new Stop(test2).apply(chatId));


        return cf;
    }
}
