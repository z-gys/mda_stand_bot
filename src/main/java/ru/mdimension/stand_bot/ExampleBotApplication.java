package ru.mdimension.stand_bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;
import ru.mdimension.stand_bot.domain.CustomTimer;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

import static ru.mdimension.stand_bot.constant.BotConstant.DEV1_COMMAND;
import static ru.mdimension.stand_bot.constant.BotConstant.DEV1_NAME;
import static ru.mdimension.stand_bot.constant.BotConstant.DEV2_COMMAND;
import static ru.mdimension.stand_bot.constant.BotConstant.DEV2_NAME;
import static ru.mdimension.stand_bot.constant.BotConstant.DEV3_COMMAND;
import static ru.mdimension.stand_bot.constant.BotConstant.DEV3_NAME;
import static ru.mdimension.stand_bot.constant.BotConstant.DEV4_COMMAND;
import static ru.mdimension.stand_bot.constant.BotConstant.DEV4_NAME;
import static ru.mdimension.stand_bot.constant.BotConstant.DEV5_COMMAND;
import static ru.mdimension.stand_bot.constant.BotConstant.DEV5_NAME;
import static ru.mdimension.stand_bot.constant.BotConstant.PRERELEASE_COMMAND;
import static ru.mdimension.stand_bot.constant.BotConstant.PRERELEASE_NAME;
import static ru.mdimension.stand_bot.constant.BotConstant.STABLE_COMMAND;
import static ru.mdimension.stand_bot.constant.BotConstant.STABLE_NAME;
import static ru.mdimension.stand_bot.constant.BotConstant.TEST1_COMMAND;
import static ru.mdimension.stand_bot.constant.BotConstant.TEST1_NAME;
import static ru.mdimension.stand_bot.constant.BotConstant.TEST2_COMMAND;
import static ru.mdimension.stand_bot.constant.BotConstant.TEST2_NAME;

@SpringBootApplication()
public class ExampleBotApplication {


    @Value("${proxy.host}")
    private String proxyHost;

    @Value("${proxy.port}")
    private Integer proxyPort;

    @Value("${proxy.user}")
    private String proxyUser;

    @Value("${proxy.password}")
    private String proxyPassword;

    public static CustomTimer dev1;
    public static CustomTimer dev2;
    public static CustomTimer dev3;
    public static CustomTimer dev4;
    public static CustomTimer dev5;
    public static CustomTimer test1;
    public static CustomTimer test2;
    public static CustomTimer prerelease;
    public static CustomTimer stable;

    @Autowired
    private ApplicationEventPublisher eventPublisher;


    private void init() {
        dev1 = new CustomTimer(DEV1_NAME, eventPublisher, DEV1_COMMAND);
        dev2 = new CustomTimer(DEV2_NAME, eventPublisher, DEV2_COMMAND);
        dev3 = new CustomTimer(DEV3_NAME, eventPublisher, DEV3_COMMAND);
        dev4 = new CustomTimer(DEV4_NAME, eventPublisher, DEV4_COMMAND);
        dev5 = new CustomTimer(DEV5_NAME, eventPublisher, DEV5_COMMAND);
        test1 = new CustomTimer(TEST1_NAME, eventPublisher, TEST1_COMMAND);
        test2 = new CustomTimer(TEST2_NAME, eventPublisher, TEST2_COMMAND);
        prerelease = new CustomTimer(PRERELEASE_NAME, eventPublisher, PRERELEASE_COMMAND);
        stable = new CustomTimer(STABLE_NAME, eventPublisher, STABLE_COMMAND);
    }

    public static void main(String[] args) {
        ApiContextInitializer.init();
        SpringApplication.run(ExampleBotApplication.class, args);
    }

    @Bean
    public DefaultBotOptions defaultBotOptions() {
        Authenticator.setDefault(new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(proxyUser, proxyPassword.toCharArray());
            }
        });

        DefaultBotOptions botOptions = ApiContext.getInstance(DefaultBotOptions.class);
        botOptions.setProxyHost(proxyHost);
        botOptions.setProxyPort(proxyPort);
        // Select proxy type: [HTTP|SOCKS4|SOCKS5] (default: NO_PROXY)
        botOptions.setProxyType(DefaultBotOptions.ProxyType.SOCKS5);
        init();
        return botOptions;
    }
}
