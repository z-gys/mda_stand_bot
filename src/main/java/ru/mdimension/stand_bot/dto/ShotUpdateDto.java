package ru.mdimension.stand_bot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ShotUpdateDto {

    private long messageId;
    private long chatId;
    private String currentUserFirstName;
}
