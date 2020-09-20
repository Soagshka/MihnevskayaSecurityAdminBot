package ru.home.security_admin_bot.bot_api;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class BotStateContext {
    private Map<BotState, InputMessageHandler> messageHandlers = new HashMap<>();

    public BotStateContext(List<InputMessageHandler> messageHandlers) {
        messageHandlers.forEach(inputMessageHandler -> {
            System.out.println("inputMessageHandler.getHandlerName() = " + inputMessageHandler.getHandlerName());
            this.messageHandlers.put(inputMessageHandler.getHandlerName(), inputMessageHandler);
        });
    }

    public SendMessage processInputMessage(BotState botState, int userId, long chatId, String text) {
        InputMessageHandler inputMessageHandler = findMessageHandler(botState);
        return inputMessageHandler.handle(userId, chatId, text);
    }

    private InputMessageHandler findMessageHandler(BotState botState) {
        if (isFillingProfileState(botState)) {
            System.out.println("MESSAGE HANDLERS : " + messageHandlers.toString());
            return messageHandlers.get(BotState.FILL_LOGIN);
        } else if (isSearchingByAutoNumber(botState)) {
            return messageHandlers.get(BotState.SEARCH_BY_AUTO_NUMBER);
        } else if (isSearchingByPhoneNumber(botState)) {
            return messageHandlers.get(BotState.SEARCH_BY_PHONE_NUMBER);
        }
        return messageHandlers.get(botState);
    }

    private boolean isFillingProfileState(BotState botState) {
        switch (botState) {
            case FILL_LOGIN:
            case ASK_LOGIN:
            case ASK_PASSWORD:
            case LOGIN_SUCCESSFULLY:
                return true;
            default:
                return false;
        }
    }

    private boolean isSearchingByAutoNumber(BotState botState) {
        switch (botState) {
            case SEARCH_BY_AUTO_NUMBER:
            case ASK_AUTO_NUMBER:
            case SEARCH_BY_AUTO_NUMBER_COMPLETED:
                return true;
            default:
                return false;
        }
    }

    private boolean isSearchingByPhoneNumber(BotState botState) {
        switch (botState) {
            case SEARCH_BY_PHONE_NUMBER:
            case ASK_PHONE_NUMBER:
            case SEARCH_BY_PHONE_NUMBER_COMPLETED:
                return true;
            default:
                return false;
        }
    }
}
