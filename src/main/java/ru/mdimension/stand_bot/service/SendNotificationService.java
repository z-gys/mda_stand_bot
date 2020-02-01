package ru.mdimension.stand_bot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.mdimension.stand_bot.domain.CustomTimer;
import ru.mdimension.stand_bot.dto.NotificationDTO;
import ru.mdimension.stand_bot.dto.NotificationType;
import ru.mdimension.stand_bot.dto.ShotUpdateDto;

@Component
@RequiredArgsConstructor
public class SendNotificationService {
    private final RabbitMQService rabbitMQService;
    private long requestChatId;

    public void send(NotificationType notificationType, ShotUpdateDto dto, CustomTimer customTimer) {
        if (notificationType.equals(NotificationType.REQUEST_STOP)) {
            requestChatId = dto.getChatId();
            System.out.println("Send sendNotStopMessage");
            NotificationDTO notificationDTO = new NotificationDTO();
            notificationDTO.setChatId(customTimer.getBookedUserName().getChatId());
            notificationDTO.setNotificationMessage("Пользователь " + dto.getCurrentUserFirstName() + " хочет занять стенд " + customTimer.getNameTitle());
            notificationDTO.setTimerStopYesCommand(customTimer.NOTIFICATION_STOP_YES_COMMAND);
            notificationDTO.setTimerStopNoCommand(customTimer.NOTIFICATION_STOP_NO_COMMAND);
            notificationDTO.setStandNameTitle(customTimer.getNameTitle());
            rabbitMQService.convertAndSend("timerStop", "", notificationDTO);
        }
        if (notificationType.equals(NotificationType.STOP_YES)) {
            System.out.println("Send YES STOP");
            NotificationDTO notificationDTO = new NotificationDTO();
            notificationDTO.setChatId(requestChatId);
            notificationDTO.setNotificationMessage("Пользователь " + dto.getCurrentUserFirstName() + " освободил стенд " + customTimer.getNameTitle());
            notificationDTO.setStandNameTitle(customTimer.getNameTitle());
            customTimer.stop();
            rabbitMQService.convertAndSend("timerStopYes", "", notificationDTO);
        }
        if (notificationType.equals(NotificationType.STOP_NO)) {
            System.out.println("Send NOT STOP");
            NotificationDTO notificationDTO = new NotificationDTO();
            notificationDTO.setChatId(requestChatId);
            notificationDTO.setNotificationMessage("Пользователь " + dto.getCurrentUserFirstName() + " отказал освободить " + customTimer.getNameTitle());
            notificationDTO.setStandNameTitle(customTimer.getNameTitle());
            rabbitMQService.convertAndSend("timerStopNo", "", notificationDTO);

        }
    }
}
