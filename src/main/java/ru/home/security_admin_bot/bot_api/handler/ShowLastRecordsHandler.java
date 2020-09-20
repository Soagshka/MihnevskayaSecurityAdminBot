package ru.home.security_admin_bot.bot_api.handler;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.home.security_admin_bot.bot_api.BotState;
import ru.home.security_admin_bot.bot_api.InputMessageHandler;
import ru.home.security_admin_bot.dao.RecordDataEntity;
import ru.home.security_admin_bot.dao.UserEntity;
import ru.home.security_admin_bot.dao.repository.RecordDataRepository;
import ru.home.security_admin_bot.dao.repository.UserEntityRepository;
import ru.home.security_admin_bot.util.BotStateUtil;

import java.util.List;
import java.util.StringJoiner;

@Component
public class ShowLastRecordsHandler implements InputMessageHandler {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(ShowLastRecordsHandler.class);
    private final RecordDataRepository recordDataRepository;
    private final UserEntityRepository userEntityRepository;

    public ShowLastRecordsHandler(RecordDataRepository recordDataRepository, UserEntityRepository userEntityRepository) {
        this.recordDataRepository = recordDataRepository;
        this.userEntityRepository = userEntityRepository;
    }

    @Override
    public SendMessage handle(Message message) {
        int userId = message.getFrom().getId();
        long chatId = message.getChatId();

        UserEntity userEntity = userEntityRepository.findByUserIdAndChatId(userId, chatId);
        if (userEntity == null) {
            return new SendMessage(chatId, "К сожалению у вас нет доступа к этой информации...");
        }
        SendMessage sendMessage;
        List<RecordDataEntity> recordDataEntityList = recordDataRepository.findTop5ByOrderByRecordDateDesc();
        if (recordDataEntityList.isEmpty()) {
            sendMessage = new SendMessage(chatId, "Нет данных о записях...");
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
            sendMessage = new SendMessage(chatId, joiner.toString());
        }

        BotStateUtil.saveBotState(userId, chatId, BotState.SHOW_MAIN_MENU);
        return sendMessage;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SHOW_5_LAST_RECORDS;
    }
}
