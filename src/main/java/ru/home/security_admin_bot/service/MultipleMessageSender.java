package ru.home.security_admin_bot.service;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.home.security_admin_bot.controller.to.RecordData;
import ru.home.security_admin_bot.dao.UserEntity;
import ru.home.security_admin_bot.dao.repository.RecordDataRepository;
import ru.home.security_admin_bot.dao.repository.UserEntityRepository;
import ru.home.security_admin_bot.mapper.RecordDataMapper;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class MultipleMessageSender {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(MultipleMessageSender.class);
    private final UserEntityRepository userEntityRepository;
    private final RecordDataRepository recordDataRepository;

    @Value("${telegrambot.botToken}")
    private String apiToken;

    public MultipleMessageSender(UserEntityRepository userEntityRepository, RecordDataRepository recordDataRepository) {
        this.userEntityRepository = userEntityRepository;
        this.recordDataRepository = recordDataRepository;
    }

    public boolean sendToTelegram(RecordData recordData) {
        List<UserEntity> userEntityList = userEntityRepository.findAll();
        try {
            for (UserEntity userEntity : userEntityList) {
                String chatId = userEntity.getChatId().toString();
                String text = "Новая заявка  \n----------------------------------------\n Номер квартиры: " + recordData.getFlatNumber() + "\n Номер телефона: " + recordData.getPhoneNumber().replaceAll("\\+", "")
                        + "\n Марка автомобиля: " + recordData.getCarMark() + "\n Номер автомобиля: " + recordData.getCarNumber();
                RestTemplate restTemplate = new RestTemplate();
                final String baseUrl = "https://api.telegram.org/bot" + apiToken + "/sendMessage?chat_id=" + chatId + "&text=" + URLEncoder.encode(text, "UTF-8");
                log.warn("baseURL = " + baseUrl);
                URI uri = new URI(baseUrl);

                restTemplate.getForEntity(uri, String.class);
                log.warn("URI = " + uri.toString());
                Thread.sleep(TimeUnit.SECONDS.toMillis(1));
            }
            recordDataRepository.save(RecordDataMapper.RECORD_DATA_MAPPER.recordDataToRecordEntity(recordData));
        } catch (InterruptedException | URISyntaxException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
