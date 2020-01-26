package ru.mdimension.stand_bot.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.mdimension.stand_bot.dto.NotificationDTO;
import ru.mdimension.stand_bot.dto.ShotUpdateDto;
import ru.mdimension.stand_bot.service.RabbitMQService;

import java.time.LocalTime;
import java.util.Timer;
import java.util.TimerTask;

@Setter
@Getter
@Slf4j
public class CustomTimer extends Stand {
    private RabbitMQService rabbitMQService;
    private String name;
    private final Timer timer = new Timer();
    private LocalTime start;
    private LocalTime stop;

    public void start(ShotUpdateDto dto) {
        super.setBookedUserName(dto);
        start = LocalTime.now();
        stop = LocalTime.now().plusHours(3);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                stop = stop.minusSeconds(1);
                if (timeLeft().compareTo(LocalTime.of(0, 10, 0, stop.getNano())) == 0) {
                    log.info("send notification timeLeftMessage" + CustomTimer.super.getName());
                    sendTimeLeftNotification();
                }
                if (timeLeft().compareTo(LocalTime.of(0, 0, 0, stop.getNano())) == 0) {
                    log.info("Timer stop for stand" + CustomTimer.super.getName());
                    stop();
                    cancel();
                }
            }
        }, 0, 1000);
    }

    public void stop() {
        super.setBookedUserName(null);
    }

    private void sendTimeLeftNotification() {
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setChatId(super.getBookedUserName().getChatId());
        notificationDTO.setTimeLeftMessage("10 минут");
        notificationDTO.setStandName(this.name);
        log.info("convert" + notificationDTO.toString());
        rabbitMQService.convertAndSend("timer", "", notificationDTO);
    }

    private LocalTime timeLeft() {
        int minute = start.getMinute();
        int hour = start.getHour();
        int second = start.getSecond();
        return stop.minusSeconds(second).minusHours(hour).minusMinutes(minute);
    }

    public CustomTimer(String name, RabbitMQService rabbitMQService) {
        this.name = name;
        this.rabbitMQService = rabbitMQService;
    }
}

