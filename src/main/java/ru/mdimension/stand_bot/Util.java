package ru.mdimension.stand_bot;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.mdimension.stand_bot.config.InlineKeyboardBuilder;
import ru.mdimension.stand_bot.domain.CustomTimer;
import ru.mdimension.stand_bot.domain.StandBusyStatus;
import ru.mdimension.stand_bot.dto.ShotUpdateDto;
import ru.mdimension.stand_bot.dto.User;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static ru.mdimension.stand_bot.ExampleBotApplication.dev1;
import static ru.mdimension.stand_bot.ExampleBotApplication.dev2;
import static ru.mdimension.stand_bot.ExampleBotApplication.dev3;
import static ru.mdimension.stand_bot.ExampleBotApplication.dev4;
import static ru.mdimension.stand_bot.ExampleBotApplication.test1;
import static ru.mdimension.stand_bot.ExampleBotApplication.test2;
import static ru.mdimension.stand_bot.constant.BotConstant.DEV1_NAME;
import static ru.mdimension.stand_bot.constant.BotConstant.DEV2_NAME;
import static ru.mdimension.stand_bot.constant.BotConstant.DEV3_NAME;
import static ru.mdimension.stand_bot.constant.BotConstant.DEV4_NAME;
import static ru.mdimension.stand_bot.constant.BotConstant.START;
import static ru.mdimension.stand_bot.constant.BotConstant.TEST1_NAME;
import static ru.mdimension.stand_bot.constant.BotConstant.TEST2_NAME;

@Component
@RequiredArgsConstructor
public class Util {

    public static ShotUpdateDto createDTO(Update update) {
        ShotUpdateDto dto = new ShotUpdateDto();
        if (update.hasCallbackQuery()) {
            dto.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
            dto.setChatId(update.getCallbackQuery().getMessage().getChatId());
            dto.setCurrentUserFirstName(update.getCallbackQuery().getMessage().getChat().getFirstName());
        } else {
            dto.setMessageId(update.getMessage().getMessageId());
            dto.setChatId(update.getMessage().getChatId());
            dto.setCurrentUserFirstName(update.getMessage().getChat().getFirstName());
        }
        return dto;
    }

    public static User createUserDTO(Update update) {
        return User.builder()
                .userId(update.getMessage().getChat().getId())
                .userName(update.getMessage().getChat().getUserName())
                .firstName(update.getMessage().getChat().getFirstName())
                .lastName(update.getMessage().getChat().getLastName())
                .role("USER")
                .build();
    }

    public static User createUserDTO(CallbackQuery update) {
        return User.builder()
                .userId(update.getMessage().getChat().getId())
                .userName(update.getMessage().getChat().getUserName())
                .firstName(update.getMessage().getChat().getFirstName())
                .lastName(update.getMessage().getChat().getLastName())
                .role("USER")
                .build();
    }

    public static SendMessage getSendMessage(long chatId, ShotUpdateDto dto, CustomTimer timer, String startCommand, String stopCommand) {
        SendMessage message;
        String currentUserFirstName = "";
        ShotUpdateDto shotUpdateDto = Optional.ofNullable(timer.getBookedUserName()).orElse(null);
        if (shotUpdateDto != null) {
            currentUserFirstName = shotUpdateDto.getCurrentUserFirstName();
        }
        String dtoUserFirstName = dto.getCurrentUserFirstName();

        if (!currentUserFirstName.isEmpty() && dtoUserFirstName != null && !currentUserFirstName.equals(dtoUserFirstName)) {
            message = InlineKeyboardBuilder.create(chatId)
                    .setText("Стенд занят пользователем " + timer.getBookedUserName().getCurrentUserFirstName() + ", \n" +
                            "освободится в " + timer.getStop().format(DateTimeFormatter.ofPattern("HH:mm")))
                    .row()
                    .button("Попросить освободить", timer.NOTIFICATION_STOP_REQUEST_COMMAND)
                    .endRow()
                    .row()
                    .button("Назад", START)
                    .endRow()
                    .build();

        } else if (!currentUserFirstName.isEmpty() && currentUserFirstName.equals(dtoUserFirstName)) {
            message = InlineKeyboardBuilder.create(chatId)
                    .setText("Что хочешь сделать?")
                    .row()
                    .button("Продлить на час", timer.PROLONG_1HOUR_COMMAND)
                    .endRow()
                    .row()
                    .button("Освободить", timer.STOP_COMMAND)
                    .endRow()
                    .row()
                    .button("Назад", START)
                    .endRow()
                    .build();
        } else {
            message = InlineKeyboardBuilder.create(chatId)
                    .setText("Стенд свободен!")
                    .row()
                    .button("Занять на 3 часа", timer.START_COMMAND)
                    .endRow()
                    .row()
                    .button("Назад", START)
                    .endRow()
                    .build();
        }
        return message;
    }

    public static String getStatusText(String standName) {
        CustomTimer timerByStandName = getTimerByStandName(standName);
        if (timerByStandName.getStandBusyStatus().equals(StandBusyStatus.STAND_FREE))
            return timerByStandName.getNameTitle() + " " + timerByStandName.getStandBusyStatus().getIcon();
        else
            return timerByStandName.getNameTitle() + " " + timerByStandName.getStandBusyStatus().getIcon()
                    + " \n" + "" + timerByStandName.getBookedUserName().getCurrentUserFirstName() + " до " + timerByStandName.getStop().format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    public static CustomTimer getTimerByStandName(String standName) {
        CustomTimer result = null;
        switch (standName) {
            case DEV1_NAME:
                result = dev1;
                break;
            case DEV2_NAME:
                result = dev2;
                break;
            case DEV3_NAME:
                result = dev3;
                break;
            case DEV4_NAME:
                result = dev4;
                break;
            case TEST1_NAME:
                result = test1;
                break;
            case TEST2_NAME:
                result = test2;
                break;
        }
        return result;
    }
}
