package ru.mdimension.stand_bot;

import org.springframework.util.StringUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.mdimension.stand_bot.config.InlineKeyboardBuilder;
import ru.mdimension.stand_bot.domain.CustomTimer;
import ru.mdimension.stand_bot.dto.ShotUpdateDto;
import ru.mdimension.stand_bot.dto.User;

import static ru.mdimension.stand_bot.constant.BotConstant.START;

public class Util {

    public static ShotUpdateDto createDTO(Update update) {
        ShotUpdateDto dto = new ShotUpdateDto();
        if (update.hasCallbackQuery()) {
            dto.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
            dto.setChatId(update.getCallbackQuery().getMessage().getChatId());
            dto.setCurrentUserFirstName(update.getCallbackQuery().getFrom().getFirstName());
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

    public static SendMessage getSendMessage(long chatId, String standName, CustomTimer timer, String startCommand, String stopCommand) {
        String text = standName + "\n"
                + "Текущий статус: Свободен";

        if (!StringUtils.isEmpty(timer.getBookedUserName())) {
            text = "Текущий статус: Занят " + timer.getBookedUserName().getCurrentUserFirstName()
                    + "\n до " + timer.getStop();
        }
        return InlineKeyboardBuilder.create(chatId)
                .setText(text)
                .row()
                .button("Занять на 3 часа", startCommand)
                .endRow()
                .row()
                .button("Освободить", stopCommand)
                .endRow()
                .row()
                .button("Назад", START)
                .endRow()
                .build();
    }
}
