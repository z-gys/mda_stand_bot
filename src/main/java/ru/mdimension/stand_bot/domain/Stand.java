package ru.mdimension.stand_bot.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.mdimension.stand_bot.dto.ShotUpdateDto;

@Getter
@Setter
@ToString
public class Stand {
    private String nameTitle;
    private String nameCommand;
    private ShotUpdateDto bookedUserName;
}

