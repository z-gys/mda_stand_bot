package ru.mdimension.stand_bot.domain;

import lombok.Getter;
import lombok.Setter;
import ru.mdimension.stand_bot.dto.ShotUpdateDto;

@Getter
@Setter
public class Stand {
    private String name;
    private ShotUpdateDto bookedUserName;
}

