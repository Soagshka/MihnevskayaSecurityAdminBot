package ru.home.security_admin_bot.controller;

import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.home.security_admin_bot.bot_api.SecurityTelegramAdminBot;
import ru.home.security_admin_bot.controller.to.RecordData;

import javax.validation.Valid;

@RestController
public class WebHookController {
    private final SecurityTelegramAdminBot securityTelegramAdminBot;

    public WebHookController(SecurityTelegramAdminBot securityTelegramAdminBot) {
        this.securityTelegramAdminBot = securityTelegramAdminBot;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update) {
        return securityTelegramAdminBot.onWebhookUpdateReceived(update);
    }

    @PostMapping(path = "send-record", consumes = "application/json", produces = "application/json")
    public BotApiMethod<?> sendRecord(@RequestBody @Valid RecordData recordData) {
        return securityTelegramAdminBot.getRecordData(recordData);
    }
}
