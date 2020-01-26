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
    public static CustomTimer test1;
    public static CustomTimer test2;

    @Autowired
    RabbitMQService rabbitMQService;


    private void init() {
        dev1 = new CustomTimer("dev1", rabbitMQService);
        dev2 = new CustomTimer("dev2", rabbitMQService);
        test1 = new CustomTimer("Test1", rabbitMQService);
        test2 = new CustomTimer("Test2", rabbitMQService);
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
