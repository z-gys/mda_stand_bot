package ru.mdimension.stand_bot.command;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.mdimension.stand_bot.command.menu.MainMenu;
import ru.mdimension.stand_bot.command.menu.action.Notification;
import ru.mdimension.stand_bot.command.menu.action.Prolong;
import ru.mdimension.stand_bot.command.menu.action.Restart;
import ru.mdimension.stand_bot.command.menu.action.Start;
import ru.mdimension.stand_bot.command.menu.action.Stop;
import ru.mdimension.stand_bot.command.menu.stand.DEV1;
import ru.mdimension.stand_bot.command.menu.stand.DEV2;
import ru.mdimension.stand_bot.command.menu.stand.DEV3;
import ru.mdimension.stand_bot.command.menu.stand.DEV4;
import ru.mdimension.stand_bot.command.menu.stand.DEV5;
import ru.mdimension.stand_bot.command.menu.stand.PRERELEASE;
import ru.mdimension.stand_bot.command.menu.stand.STABLE;
import ru.mdimension.stand_bot.command.menu.stand.Test1;
import ru.mdimension.stand_bot.command.menu.stand.Test2;
import ru.mdimension.stand_bot.dto.NotificationType;
import ru.mdimension.stand_bot.dto.ShotUpdateDto;
import ru.mdimension.stand_bot.service.SendNotificationService;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.mdimension.stand_bot.ExampleBotApplication.dev1;
import static ru.mdimension.stand_bot.ExampleBotApplication.dev2;
import static ru.mdimension.stand_bot.ExampleBotApplication.dev3;
import static ru.mdimension.stand_bot.ExampleBotApplication.dev4;
import static ru.mdimension.stand_bot.ExampleBotApplication.dev5;
import static ru.mdimension.stand_bot.ExampleBotApplication.prerelease;
import static ru.mdimension.stand_bot.ExampleBotApplication.stable;
import static ru.mdimension.stand_bot.ExampleBotApplication.test1;
import static ru.mdimension.stand_bot.ExampleBotApplication.test2;
import static ru.mdimension.stand_bot.constant.BotConstant.RESTART;
import static ru.mdimension.stand_bot.constant.BotConstant.START;

@Component
public class CommandFactory {
    private final Map<String, Command> commands;
    private final SendNotificationService notificationService;

    protected CommandFactory(SendNotificationService notificationService) {
        commands = new HashMap<>();
        this.notificationService = notificationService;
    }

    public void addCommand(final String name, final Command command) {
        commands.put(name, command);
    }

    public SendMessage executeCommand(String name, long chatId) {
        if (commands.containsKey(name)) {
            return commands.get(name).apply(chatId);
        } else {
            SendMessage error = new SendMessage();
            error.setChatId(chatId);
            error.setText("Ой, такой команды я не знаю!");
            return error;
        }
    }

    public void listCommands() {
        System.out.println("Enabled commands: " + commands.keySet().stream().collect(Collectors.joining(", ")));
    }

    /* Factory pattern */
    public static CommandFactory init(Long chatId, ShotUpdateDto dto, SendNotificationService notificationService) {

        final CommandFactory cf = new CommandFactory(notificationService);

        // commands are added here using lambdas. It is also possible to dynamically add commands without editing the code.
        cf.addCommand(START, (c) -> new MainMenu(dto).apply(chatId));
        cf.addCommand(RESTART, (c) -> new Restart(dto).apply(chatId));

        cf.addCommand(dev1.INFO_COMMAND, (c) -> new DEV1(dto).apply(chatId));
        cf.addCommand(dev1.START_COMMAND, (c) -> new Start(dto, dev1).apply(chatId));
        cf.addCommand(dev1.STOP_COMMAND, (c) -> new Stop(dev1).apply(chatId));
        cf.addCommand(dev1.NOTIFICATION_STOP_REQUEST_COMMAND, (c) -> new Notification(dto, dev1, notificationService, NotificationType.REQUEST_STOP).apply(chatId));
        cf.addCommand(dev1.NOTIFICATION_STOP_NO_COMMAND, (c) -> new Notification(dto, dev1, notificationService, NotificationType.STOP_NO).apply(chatId));
        cf.addCommand(dev1.NOTIFICATION_STOP_YES_COMMAND, (c) -> new Notification(dto, dev1, notificationService, NotificationType.STOP_YES).apply(chatId));
        cf.addCommand(dev1.PROLONG_1HOUR_COMMAND, (c) -> new Prolong(dev1).apply(chatId));


        cf.addCommand(dev2.INFO_COMMAND, (c) -> new DEV2(dto).apply(chatId));
        cf.addCommand(dev2.START_COMMAND, (c) -> new Start(dto, dev2).apply(chatId));
        cf.addCommand(dev2.STOP_COMMAND, (c) -> new Stop(dev2).apply(chatId));
        cf.addCommand(dev2.NOTIFICATION_STOP_REQUEST_COMMAND, (c) -> new Notification(dto, dev2, notificationService, NotificationType.REQUEST_STOP).apply(chatId));
        cf.addCommand(dev2.NOTIFICATION_STOP_NO_COMMAND, (c) -> new Notification(dto, dev2, notificationService, NotificationType.STOP_NO).apply(chatId));
        cf.addCommand(dev2.NOTIFICATION_STOP_YES_COMMAND, (c) -> new Notification(dto, dev2, notificationService, NotificationType.STOP_YES).apply(chatId));
        cf.addCommand(dev2.PROLONG_1HOUR_COMMAND, (c) -> new Prolong(dev2).apply(chatId));


        cf.addCommand(dev3.INFO_COMMAND, (c) -> new DEV3(dto).apply(chatId));
        cf.addCommand(dev3.START_COMMAND, (c) -> new Start(dto, dev3).apply(chatId));
        cf.addCommand(dev3.STOP_COMMAND, (c) -> new Stop(dev3).apply(chatId));
        cf.addCommand(dev3.NOTIFICATION_STOP_REQUEST_COMMAND, (c) -> new Notification(dto, dev3, notificationService, NotificationType.REQUEST_STOP).apply(chatId));
        cf.addCommand(dev3.NOTIFICATION_STOP_NO_COMMAND, (c) -> new Notification(dto, dev3, notificationService, NotificationType.STOP_NO).apply(chatId));
        cf.addCommand(dev3.NOTIFICATION_STOP_YES_COMMAND, (c) -> new Notification(dto, dev3, notificationService, NotificationType.STOP_YES).apply(chatId));
        cf.addCommand(dev3.PROLONG_1HOUR_COMMAND, (c) -> new Prolong(dev3).apply(chatId));

        cf.addCommand(dev4.INFO_COMMAND, (c) -> new DEV4(dto).apply(chatId));
        cf.addCommand(dev4.START_COMMAND, (c) -> new Start(dto, dev4).apply(chatId));
        cf.addCommand(dev4.STOP_COMMAND, (c) -> new Stop(dev4).apply(chatId));
        cf.addCommand(dev4.NOTIFICATION_STOP_REQUEST_COMMAND, (c) -> new Notification(dto, dev4, notificationService, NotificationType.REQUEST_STOP).apply(chatId));
        cf.addCommand(dev4.NOTIFICATION_STOP_NO_COMMAND, (c) -> new Notification(dto, dev4, notificationService, NotificationType.STOP_NO).apply(chatId));
        cf.addCommand(dev4.NOTIFICATION_STOP_YES_COMMAND, (c) -> new Notification(dto, dev4, notificationService, NotificationType.STOP_YES).apply(chatId));
        cf.addCommand(dev4.PROLONG_1HOUR_COMMAND, (c) -> new Prolong(dev4).apply(chatId));


        cf.addCommand(dev5.INFO_COMMAND, (c) -> new DEV5(dto).apply(chatId));
        cf.addCommand(dev5.START_COMMAND, (c) -> new Start(dto, dev5).apply(chatId));
        cf.addCommand(dev5.STOP_COMMAND, (c) -> new Stop(dev5).apply(chatId));
        cf.addCommand(dev5.NOTIFICATION_STOP_REQUEST_COMMAND, (c) -> new Notification(dto, dev5, notificationService, NotificationType.REQUEST_STOP).apply(chatId));
        cf.addCommand(dev5.NOTIFICATION_STOP_NO_COMMAND, (c) -> new Notification(dto, dev5, notificationService, NotificationType.STOP_NO).apply(chatId));
        cf.addCommand(dev5.NOTIFICATION_STOP_YES_COMMAND, (c) -> new Notification(dto, dev5, notificationService, NotificationType.STOP_YES).apply(chatId));
        cf.addCommand(dev5.PROLONG_1HOUR_COMMAND, (c) -> new Prolong(dev5).apply(chatId));

        cf.addCommand(test1.INFO_COMMAND, (c) -> new Test1(dto).apply(chatId));
        cf.addCommand(test1.START_COMMAND, (c) -> new Start(dto, test1).apply(chatId));
        cf.addCommand(test1.STOP_COMMAND, (c) -> new Stop(test1).apply(chatId));
        cf.addCommand(test1.NOTIFICATION_STOP_REQUEST_COMMAND, (c) -> new Notification(dto, test1, notificationService, NotificationType.REQUEST_STOP).apply(chatId));
        cf.addCommand(test1.NOTIFICATION_STOP_NO_COMMAND, (c) -> new Notification(dto, test1, notificationService, NotificationType.STOP_NO).apply(chatId));
        cf.addCommand(test1.NOTIFICATION_STOP_YES_COMMAND, (c) -> new Notification(dto, test1, notificationService, NotificationType.STOP_YES).apply(chatId));
        cf.addCommand(test1.PROLONG_1HOUR_COMMAND, (c) -> new Prolong(test1).apply(chatId));

        cf.addCommand(test2.INFO_COMMAND, (c) -> new Test2(dto).apply(chatId));
        cf.addCommand(test2.START_COMMAND, (c) -> new Start(dto, test2).apply(chatId));
        cf.addCommand(test2.STOP_COMMAND, (c) -> new Stop(test2).apply(chatId));
        cf.addCommand(test2.NOTIFICATION_STOP_REQUEST_COMMAND, (c) -> new Notification(dto, test2, notificationService, NotificationType.REQUEST_STOP).apply(chatId));
        cf.addCommand(test2.NOTIFICATION_STOP_NO_COMMAND, (c) -> new Notification(dto, test2, notificationService, NotificationType.STOP_NO).apply(chatId));
        cf.addCommand(test2.NOTIFICATION_STOP_YES_COMMAND, (c) -> new Notification(dto, test2, notificationService, NotificationType.STOP_YES).apply(chatId));
        cf.addCommand(test2.PROLONG_1HOUR_COMMAND, (c) -> new Prolong(test2).apply(chatId));

        cf.addCommand(stable.INFO_COMMAND, (c) -> new STABLE(dto).apply(chatId));
        cf.addCommand(stable.START_COMMAND, (c) -> new Start(dto, stable).apply(chatId));
        cf.addCommand(stable.STOP_COMMAND, (c) -> new Stop(stable).apply(chatId));
        cf.addCommand(stable.NOTIFICATION_STOP_REQUEST_COMMAND, (c) -> new Notification(dto, stable, notificationService, NotificationType.REQUEST_STOP).apply(chatId));
        cf.addCommand(stable.NOTIFICATION_STOP_NO_COMMAND, (c) -> new Notification(dto, stable, notificationService, NotificationType.STOP_NO).apply(chatId));
        cf.addCommand(stable.NOTIFICATION_STOP_YES_COMMAND, (c) -> new Notification(dto, stable, notificationService, NotificationType.STOP_YES).apply(chatId));
        cf.addCommand(stable.PROLONG_1HOUR_COMMAND, (c) -> new Prolong(stable).apply(chatId));

        cf.addCommand(prerelease.INFO_COMMAND, (c) -> new PRERELEASE(dto).apply(chatId));
        cf.addCommand(prerelease.START_COMMAND, (c) -> new Start(dto, prerelease).apply(chatId));
        cf.addCommand(prerelease.STOP_COMMAND, (c) -> new Stop(prerelease).apply(chatId));
        cf.addCommand(prerelease.NOTIFICATION_STOP_REQUEST_COMMAND, (c) -> new Notification(dto, prerelease, notificationService, NotificationType.REQUEST_STOP).apply(chatId));
        cf.addCommand(prerelease.NOTIFICATION_STOP_NO_COMMAND, (c) -> new Notification(dto, prerelease, notificationService, NotificationType.STOP_NO).apply(chatId));
        cf.addCommand(prerelease.NOTIFICATION_STOP_YES_COMMAND, (c) -> new Notification(dto, prerelease, notificationService, NotificationType.STOP_YES).apply(chatId));
        cf.addCommand(prerelease.PROLONG_1HOUR_COMMAND, (c) -> new Prolong(prerelease).apply(chatId));

        return cf;
    }
}
