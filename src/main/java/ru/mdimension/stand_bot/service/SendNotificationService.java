package ru.mdimension.stand_bot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import ru.mdimension.stand_bot.domain.CustomTimer;
import ru.mdimension.stand_bot.dto.NotificationCommand;
import ru.mdimension.stand_bot.dto.NotificationDTO;
import ru.mdimension.stand_bot.dto.NotificationType;
import ru.mdimension.stand_bot.dto.ShotUpdateDto;

@Component
@RequiredArgsConstructor
public class SendNotificationService {
    private final ApplicationEventPublisher eventPublisher;
    private long requestChatId;

    public void send(NotificationType notificationType, ShotUpdateDto dto, CustomTimer customTimer) {
        if (notificationType.equals(NotificationType.REQUEST_STOP)) {
            requestChatId = dto.getChatId();
            System.out.println("Send sendNotStopMessage");
            NotificationDTO notification = new NotificationDTO();
            notification.setChatId(customTimer.getBookedUserName().getChatId());
            notification.setNotificationMessage("Пользователь " + dto.getCurrentUserFirstName() + " хочет занять стенд " + customTimer.getNameTitle());
            notification.setTimerStopYesCommand(customTimer.NOTIFICATION_STOP_YES_COMMAND);
            notification.setTimerStopNoCommand(customTimer.NOTIFICATION_STOP_NO_COMMAND);
            notification.setStandNameTitle(customTimer.getNameTitle());
            notification.setCommand(NotificationCommand.TIMER_STOP);
            eventPublisher.publishEvent(notification);
        }
        if (notificationType.equals(NotificationType.STOP_YES)) {
            System.out.println("Send YES STOP");
            NotificationDTO notification = new NotificationDTO();
            notification.setChatId(requestChatId);
            notification.setNotificationMessage("Пользователь " + dto.getCurrentUserFirstName() + " освободил стенд " + customTimer.getNameTitle());
            notification.setStandNameTitle(customTimer.getNameTitle());
            notification.setCommand(NotificationCommand.TIMER_STOP_YES);
            customTimer.stop();
            eventPublisher.publishEvent(notification);
        }
        if (notificationType.equals(NotificationType.STOP_NO)) {
            System.out.println("Send NOT STOP");
            NotificationDTO notification = new NotificationDTO();
            notification.setChatId(requestChatId);
            notification.setNotificationMessage("Пользователь " + dto.getCurrentUserFirstName() + " отказал освободить " + customTimer.getNameTitle());
            notification.setStandNameTitle(customTimer.getNameTitle());
            notification.setCommand(NotificationCommand.TIMER_STOP_NO);
            eventPublisher.publishEvent(notification);

        }
    }
}
