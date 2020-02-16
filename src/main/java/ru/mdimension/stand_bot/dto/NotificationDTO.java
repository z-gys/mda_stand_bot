package ru.mdimension.stand_bot.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NotificationDTO {
    private long chatId;
    private String notificationMessage;
    private String standNameTitle;
    //private CustomTimer customTimer;
    private String timerCommand;
    private String timerStopYesCommand;
    private String timerStopNoCommand;
    private NotificationCommand command;
}
