package ru.mdimension.stand_bot.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class User {
    long userId;
    String firstName;
    String lastName;
    String userName;
    String role = "USER";

}
