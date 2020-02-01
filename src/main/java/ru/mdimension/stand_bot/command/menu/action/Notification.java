package ru.mdimension.stand_bot.command.menu.action;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.mdimension.stand_bot.command.Command;
import ru.mdimension.stand_bot.config.InlineKeyboardBuilder;
import ru.mdimension.stand_bot.domain.CustomTimer;
import ru.mdimension.stand_bot.dto.NotificationType;
import ru.mdimension.stand_bot.dto.ShotUpdateDto;
import ru.mdimension.stand_bot.service.SendNotificationService;

import static ru.mdimension.stand_bot.constant.BotConstant.START;

public class Notification implements Command {
    private ShotUpdateDto updateDto;
    private SendNotificationService notificationService;
    private CustomTimer customTimer;
    private NotificationType notificationType;

    @Override
    public SendMessage apply(long chatId) {
        notificationService.send(notificationType, updateDto, customTimer);
        return InlineKeyboardBuilder.create(chatId)
                .setText("Уведомление отправлено!")
                .row()
                .button("Назад", START)
                .endRow()
                .build();
    }

    public Notification(ShotUpdateDto updateDto, CustomTimer customTimer, SendNotificationService notificationService, NotificationType notificationType) {
        this.updateDto = updateDto;
        this.customTimer = customTimer;
        this.notificationService = notificationService;
        this.notificationType = notificationType;
    }
}
