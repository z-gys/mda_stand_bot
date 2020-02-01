package ru.mdimension.stand_bot;

import com.github.xabgesagtx.bots.TelegramBotAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;
import ru.mdimension.stand_bot.domain.CustomTimer;
import ru.mdimension.stand_bot.service.RabbitMQService;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

import static ru.mdimension.stand_bot.constant.BotConstant.DEV1_NAME;
import static ru.mdimension.stand_bot.constant.BotConstant.DEV2_NAME;
import static ru.mdimension.stand_bot.constant.BotConstant.DEV3_NAME;
import static ru.mdimension.stand_bot.constant.BotConstant.DEV4_NAME;
import static ru.mdimension.stand_bot.constant.BotConstant.TEST1_NAME;
import static ru.mdimension.stand_bot.constant.BotConstant.TEST2_NAME;

@SpringBootApplication
@EnableAutoConfiguration(exclude = TelegramBotAutoConfiguration.class)
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
    public static CustomTimer test1;
    public static CustomTimer test2;

    @Autowired
    RabbitMQService rabbitMQService;


    private void init() {
        dev1 = new CustomTimer(DEV1_NAME, rabbitMQService, "/dev1");
        dev2 = new CustomTimer(DEV2_NAME, rabbitMQService, "/dev2");
        dev3 = new CustomTimer(DEV3_NAME, rabbitMQService, "/dev3");
        dev4 = new CustomTimer(DEV4_NAME, rabbitMQService, "/dev4");
        test1 = new CustomTimer(TEST1_NAME, rabbitMQService, "/test1");
        test2 = new CustomTimer(TEST2_NAME, rabbitMQService, "/test2");
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
