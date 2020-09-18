package ru.home.security_admin_bot.bot_api;

import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.home.security_admin_bot.controller.to.RecordData;

public class SecurityTelegramAdminBot extends TelegramWebhookBot {
    private String botToken;
    private String botUserName;
    private String webHookPath;

    private TelegramFacade telegramFacade;

    public SecurityTelegramAdminBot(TelegramFacade telegramFacade) {
        this.telegramFacade = telegramFacade;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        return telegramFacade.handleUpdate(update);
    }

    @Override
    public String getBotUsername() {
        return botUserName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotPath() {
        return webHookPath;
    }

    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }

    public void setBotUserName(String botUserName) {
        this.botUserName = botUserName;
    }

    public void setWebHookPath(String webHookPath) {
        this.webHookPath = webHookPath;
    }

    public String sendMessages(RecordData recordData) {
        return telegramFacade.sendRecordToUsers(recordData);
    }
}
