package ru.home.security_admin_bot.util;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.home.security_admin_bot.bot_api.BotState;
import ru.home.security_admin_bot.dao.BotStateEntity;
import ru.home.security_admin_bot.dao.RecordDataEntity;
import ru.home.security_admin_bot.dao.repository.BotStateRepository;

import java.util.List;
import java.util.StringJoiner;

@Component
public class BotStateUtil {
    private static BotStateRepository botStateRepository;

    public BotStateUtil(BotStateRepository botStateRepository) {
        BotStateUtil.botStateRepository = botStateRepository;
    }

    public static void saveBotState(int userId, Long chatId, BotState botState) {
        BotStateEntity botStateEntity = botStateRepository.findByUserIdAndChatId(userId, chatId);
        if (botStateEntity != null) {
            botStateEntity.setBotState(botState.getDescription());
            botStateRepository.save(botStateEntity);
        } else {
            botStateRepository.save(new BotStateEntity(userId, chatId, botState.getDescription()));
        }
    }

    public static BotState getBotState(int userId, Long chatId) {
        BotStateEntity botStateEntity = botStateRepository.findByUserIdAndChatId(userId, chatId);
        if (botStateEntity == null) {
            return BotState.FILL_LOGIN;
        } else if (botStateEntity.getBotState().equals("FILL_LOGIN")) {
            return BotState.ASK_LOGIN;
        } else if (botStateEntity.getBotState().equals("SEARCH_BY_PHONE_NUMBER")) {
            return BotState.ASK_PHONE_NUMBER;
        } else if (botStateEntity.getBotState().equals("SEARCH_BY_AUTO_NUMBER")) {
            return BotState.ASK_AUTO_NUMBER;
        }
        return BotState.valueOf(botStateEntity.getBotState());
    }

    public static SendMessage createSendMessage(Long chatId, List<RecordDataEntity> recordDataEntityList) {
        if (recordDataEntityList.isEmpty()) {
            return new SendMessage(chatId, "Нет данных о записях...");
        } else {
            int recordsCount = 1;
            StringJoiner joiner = new StringJoiner("\n\n");
            joiner.add("Последние 5 заявок : ");
            for (RecordDataEntity recordDataEntity : recordDataEntityList) {
                joiner.add("Заявка номер " + recordsCount + " \n----------------------------------------\n Номер квартиры: "
                        + recordDataEntity.getFlatNumber() + "\n Номер телефона: "
                        + recordDataEntity.getPhoneNumber().replaceAll("\\+", "")
                        + "\n Марка автомобиля: " + recordDataEntity.getCarMark() + "\n Номер автомобиля: "
                        + recordDataEntity.getCarNumber());
                recordsCount++;
            }
            return new SendMessage(chatId, joiner.toString());
        }
    }

    public static boolean isAllowedToUse(BotState botState) {
        switch (botState) {
            case SHOW_MAIN_MENU:
            case FILL_LOGIN:
            case ASK_LOGIN:
            case ASK_PASSWORD:
            case LOGIN_SUCCESSFULLY:
                return false;
            default:
                return true;
        }
    }
}
