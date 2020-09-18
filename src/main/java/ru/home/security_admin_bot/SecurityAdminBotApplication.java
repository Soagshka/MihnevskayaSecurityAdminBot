package ru.home.security_admin_bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.telegram.telegrambots.ApiContextInitializer;
import ru.home.security_admin_bot.config.BotConfig;

@SpringBootApplication
@EnableConfigurationProperties(BotConfig.class)
public class SecurityAdminBotApplication {

    public static void main(String[] args) {
        ApiContextInitializer.init();
        SpringApplication.run(SecurityAdminBotApplication.class, args);
    }

}
