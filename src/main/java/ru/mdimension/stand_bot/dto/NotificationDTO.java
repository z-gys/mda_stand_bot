package ru.mdimension.stand_bot.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NotificationDTO {
    private String standName;
    private long chatId;
    private String timeLeftMessage;
}
