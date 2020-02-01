package ru.mdimension.stand_bot.domain;


import lombok.Getter;

@Getter
public enum StandBusyStatus {
    STAND_BUSY(" ⏰ "), STAND_FREE(" ✅ ");

    private String icon;

    StandBusyStatus(String icon) {
        this.icon = icon;
    }
}
