package ru.mdimension.stand_bot.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
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
@ToString
public class CustomTimer extends Stand {
    @JsonIgnore
    private RabbitMQService rabbitMQService;
    @JsonIgnore
    private final Timer timer = new Timer();
    private LocalTime start;
    private LocalTime stop;
    private StandAvaliableStatus standAvaliableStatus = StandAvaliableStatus.AVALIABLE;
    private StandBusyStatus standBusyStatus = StandBusyStatus.STAND_FREE;
    private boolean isSentNotification;

    public String NOTIFICATION_STOP_REQUEST_COMMAND = "/notification/stop/request/";
    public String NOTIFICATION_STOP_YES_COMMAND = "/notification/stop/yes/";
    public String NOTIFICATION_STOP_NO_COMMAND = "/notification/stop/no/";
    public String START_COMMAND = "/start/";
    public String PROLONG_1HOUR_COMMAND = "/start/1/";
    public String INFO_COMMAND = "/info/";
    public String STOP_COMMAND = "/stop/";

    public void start(ShotUpdateDto dto) {
        setBookedUserName(dto);
        standBusyStatus = StandBusyStatus.STAND_BUSY;
        start = LocalTime.now();
        stop = LocalTime.now().plusHours(3);
        isSentNotification = false;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (LocalTime.now().isAfter(stop.minusMinutes(10)) && !isSentNotification) {
                    isSentNotification = true;
                    sendTimeLeftNotification();
                }
                if (LocalTime.now().isAfter(stop)) {
                    stop();
                }
            }
        }, 1, 60000);
    }

    public void prolong1Hour() {
        isSentNotification = false;
        stop = stop.plusHours(1);
    }


    public void stop() {
        this.timer.purge();
        standBusyStatus = StandBusyStatus.STAND_FREE;
        setBookedUserName(null);
    }

    private void sendTimeLeftNotification() {
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setChatId(getBookedUserName().getChatId());
        notificationDTO.setNotificationMessage("10 минут");
        notificationDTO.setStandNameTitle(getNameTitle());
        notificationDTO.setTimerStopNoCommand(PROLONG_1HOUR_COMMAND);
        notificationDTO.setTimerStopYesCommand(STOP_COMMAND);
        log.info("convert" + notificationDTO.toString());
        rabbitMQService.convertAndSend("timer", "", notificationDTO);
    }

    public CustomTimer(String nameTitle, RabbitMQService rabbitMQService, String nameCommand) {
        setNameTitle(nameTitle);
        this.rabbitMQService = rabbitMQService;
        setNameCommand(nameCommand);

        START_COMMAND = nameCommand + START_COMMAND;
        INFO_COMMAND = nameCommand + INFO_COMMAND;
        STOP_COMMAND = nameCommand + STOP_COMMAND;

        PROLONG_1HOUR_COMMAND = nameCommand + PROLONG_1HOUR_COMMAND;

        NOTIFICATION_STOP_REQUEST_COMMAND = nameCommand + NOTIFICATION_STOP_REQUEST_COMMAND;
        NOTIFICATION_STOP_YES_COMMAND = nameCommand + NOTIFICATION_STOP_YES_COMMAND;
        NOTIFICATION_STOP_NO_COMMAND = nameCommand + NOTIFICATION_STOP_NO_COMMAND;
    }
}

