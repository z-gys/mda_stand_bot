package ru.mdimension.stand_bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
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
import ru.mdimension.stand_bot.service.RabbitMQService;
import ru.mdimension.stand_bot.service.UserService;

import static ru.mdimension.stand_bot.ExampleBotApplication.dev1;
import static ru.mdimension.stand_bot.ExampleBotApplication.dev2;
import static ru.mdimension.stand_bot.ExampleBotApplication.test1;
import static ru.mdimension.stand_bot.ExampleBotApplication.test2;
import static ru.mdimension.stand_bot.Util.createDTO;
import static ru.mdimension.stand_bot.constant.BotConstant.DEV1_COMMAND;
import static ru.mdimension.stand_bot.constant.BotConstant.DEV1_NAME;
import static ru.mdimension.stand_bot.constant.BotConstant.DEV2_COMMAND;
import static ru.mdimension.stand_bot.constant.BotConstant.DEV2_NAME;
import static ru.mdimension.stand_bot.constant.BotConstant.START;
import static ru.mdimension.stand_bot.constant.BotConstant.TEST1_COMMAND;
import static ru.mdimension.stand_bot.constant.BotConstant.TEST1_NAME;
import static ru.mdimension.stand_bot.constant.BotConstant.TEST2_COMMAND;
import static ru.mdimension.stand_bot.constant.BotConstant.TEST2_NAME;


@Component
@Slf4j
public class ExampleBot extends TelegramLongPollingBot {

    public ExampleBot(DefaultBotOptions options) {
        super(options);
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

    @Autowired
    UserService userService;

    @Autowired
    RabbitMQService rabbitMQService;


    @Override
    public void onUpdateReceived(Update update) {
        ShotUpdateDto dto = createDTO(update);
        if (update.hasMessage() && update.getMessage().getText().equals(START)) {
            userService.saveUserIfNotExist(update);
            mainMenu(update);
        }
        if (update.hasCallbackQuery()) {
            String callBackData = update.getCallbackQuery().getData();
            try {
                CommandFactory cf = CommandFactory.init(dto.getChatId(), dto);
                replaceMessage(dto, cf.executeCommand(callBackData, dto.getChatId(), dto));
            } catch (NullPointerException ignored) {
            }
        }
    }

    @RabbitListener(queues = "stop")
    public void listen(Message dto) {
        NotificationDTO objectFromMessage = rabbitMQService.getObjectFromMessage(dto, NotificationDTO.class);
        System.out.println("get timeLeftMessage" + objectFromMessage.toString());
        check(objectFromMessage);
    }

    public void check(NotificationDTO dto) {
        sendMessage("Стенд " + dto.getStandName() + " будет освобожден через " + dto.getTimeLeftMessage(), dto.getChatId());
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
                        + update.getMessage().getFrom().getFirstName() + " рад снова тебя видеть!")
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
        try {
            sendApiMethod(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    public static String getStatusText(String standName) {
        String butName = standName + "\n"
                + "Текущий статус: Свободен";

        switch (standName) {
            case "Dev 1": {
                if (!StringUtils.isEmpty(dev1.getBookedUserName())) {
                    butName = standName + "\n "
                            + "Текущий статус: Занят ";
                }
            }
            break;
            case "Dev 2": {
                if (!StringUtils.isEmpty(dev2.getBookedUserName())) {
                    butName = standName + "\n "
                            + "Текущий статус: Занят ";
                }
            }
            break;
            case "Test 1": {
                if (!StringUtils.isEmpty(test1.getBookedUserName())) {
                    butName = standName + "\n "
                            + "Текущий статус: Занят ";
                }
            }
            break;
            case "Test 2": {
                if (!StringUtils.isEmpty(test2.getBookedUserName())) {
                    butName = standName + "\n "
                            + "Текущий статус: Занят ";
                }
            }
            break;
        }
        return butName;
    }
}