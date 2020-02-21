package ru.mdimension.stand_bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.mdimension.stand_bot.command.CommandFactory;
import ru.mdimension.stand_bot.config.InlineKeyboardBuilder;
import ru.mdimension.stand_bot.dto.NotificationDTO;
import ru.mdimension.stand_bot.dto.ShotUpdateDto;
import ru.mdimension.stand_bot.service.SendNotificationService;
import ru.mdimension.stand_bot.service.UserService;

import static ru.mdimension.stand_bot.ExampleBotApplication.dev1;
import static ru.mdimension.stand_bot.ExampleBotApplication.dev2;
import static ru.mdimension.stand_bot.ExampleBotApplication.dev3;
import static ru.mdimension.stand_bot.ExampleBotApplication.dev4;
import static ru.mdimension.stand_bot.ExampleBotApplication.dev5;
import static ru.mdimension.stand_bot.ExampleBotApplication.prerelease;
import static ru.mdimension.stand_bot.ExampleBotApplication.stable;
import static ru.mdimension.stand_bot.ExampleBotApplication.test1;
import static ru.mdimension.stand_bot.ExampleBotApplication.test2;
import static ru.mdimension.stand_bot.Util.createDTO;
import static ru.mdimension.stand_bot.Util.getStatusText;
import static ru.mdimension.stand_bot.constant.BotConstant.RESTART;
import static ru.mdimension.stand_bot.constant.BotConstant.START;


@Component
@Slf4j

public class ExampleBot extends TelegramLongPollingBot {

    public ExampleBot(DefaultBotOptions options, UserService userService, SendNotificationService sendNotificationService) {
        super(options);
        this.userService = userService;
        this.sendNotificationService = sendNotificationService;
    }

    @Value("${bot.token}")
    private String token;

    @Value("${bot.username}")
    private String username;

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    private final UserService userService;

    private final SendNotificationService sendNotificationService;

    @Override
    public void onUpdateReceived(Update update) {
        ShotUpdateDto dto = createDTO(update);
        if (update.hasMessage() && update.getMessage().getText().equals(START)) {
            userService.saveUserIfNotExist(update);
            System.out.println("get update" + update.getMessage());
            mainMenu(update);
        }
        if (update.hasCallbackQuery()) {
            String callBackData = update.getCallbackQuery().getData();
            System.out.println(callBackData);
            try {
                CommandFactory cf = CommandFactory.init(dto.getChatId(), dto, sendNotificationService);
                replaceMessage(dto, cf.executeCommand(callBackData, dto.getChatId()));
            } catch (NullPointerException ignored) {
            }
        }
    }

    @EventListener
    public void listen(NotificationDTO notification) {
        switch (notification.getCommand()) {
            case STOP:
                listenStop(notification);
                break;
            case TIMER:
                listenTimer(notification);
                break;
            case TIMER_STOP:
                listenTimerStop(notification);
                break;
            case TIMER_STOP_YES:
                listenTimerStopYes(notification);
                break;
            case TIMER_STOP_NO:
                listenTimerStopNo(notification);
                break;
            default:
                log.warn("New command detected: " + notification.getCommand());
        }
    }

    private void listenTimer(NotificationDTO notification) {
        log.info("I'm should send notification, but I wan't");
    }

    public void listenStop(NotificationDTO notification) {
        SendMessage build = InlineKeyboardBuilder.create(notification.getChatId())
                .setText("Стенд " + notification.getStandNameTitle() + " будет освобожден через " + notification.getNotificationMessage())
                .row()
                .button("Продлить на час", notification.getTimerStopNoCommand())
                .button("Освободить", notification.getTimerStopYesCommand())
                .endRow()
                .build();
        sendMessage(build);
    }

    public void listenTimerStop(NotificationDTO notification) {
        SendMessage build = InlineKeyboardBuilder.create(notification.getChatId())
                .setText(notification.getNotificationMessage())
                .row()
                .button("Освободить", notification.getTimerStopYesCommand())
                .button("Отказать", notification.getTimerStopNoCommand())
                .endRow()
                .build();
        sendMessage(build);
    }

    public void listenTimerStopYes(NotificationDTO notification) {
        log.info("timerStopYes=" + notification.toString());
        sendMessage(notification.getNotificationMessage(), notification.getChatId());
    }

    public void listenTimerStopNo(NotificationDTO notification) {
        log.info("timerStopNo=" + notification.toString());
        sendMessage(notification.getNotificationMessage(), notification.getChatId());
    }


    private void sendMessage(String messageText, long chatId) {
        try {
            SendMessage message = new SendMessage();
            message.setText(messageText);
            message.setChatId(chatId);
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void replaceMessage(ShotUpdateDto dto, SendMessage message) {
        EditMessageText newMessage = new EditMessageText()
                .setChatId(dto.getChatId())
                .setMessageId(Math.toIntExact(dto.getMessageId()))
                .setText(message.getText())
                .setReplyMarkup((InlineKeyboardMarkup) message.getReplyMarkup());
        try {
            execute(newMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void mainMenu(Update update) {
        Long chatId = update.getMessage().getChatId();
        SendMessage message = InlineKeyboardBuilder.create(chatId)
                .setText("Привет, "
                        + update.getMessage().getFrom().getFirstName() + ", рад снова тебя видеть!")
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
                .button("Обновить", RESTART)
                .endRow()
                .build();
        try {
            sendApiMethod(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }


}