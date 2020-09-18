package ru.home.security_admin_bot.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.home.security_admin_bot.bot_api.SecurityTelegramAdminBot;
import ru.home.security_admin_bot.bot_api.TelegramFacade;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "telegrambot")
public class BotConfig {
    private String webHookPath;
    private String botUserName;
    private String botToken;

    @Bean
    public SecurityTelegramAdminBot securityTelegramBot(TelegramFacade telegramFacade) {

        SecurityTelegramAdminBot securityTelegramBot = new SecurityTelegramAdminBot(telegramFacade);
        securityTelegramBot.setBotUserName(botUserName);
        securityTelegramBot.setBotToken(botToken);
        securityTelegramBot.setWebHookPath(webHookPath);

        return securityTelegramBot;
    }
}
