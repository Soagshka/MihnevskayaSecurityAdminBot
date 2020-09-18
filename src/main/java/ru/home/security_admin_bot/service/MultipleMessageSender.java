package ru.home.security_admin_bot.service;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.home.security_admin_bot.controller.to.RecordData;
import ru.home.security_admin_bot.dao.UserEntity;
import ru.home.security_admin_bot.dao.repository.UserEntityRepository;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class MultipleMessageSender {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(MultipleMessageSender.class);
    private final UserEntityRepository userEntityRepository;

    @Value("${telegrambot.botToken}")
    private String apiToken;

    public MultipleMessageSender(UserEntityRepository userEntityRepository) {
        this.userEntityRepository = userEntityRepository;
    }

    public boolean sendToTelegram(RecordData recordData) {
        List<UserEntity> userEntityList = userEntityRepository.findAll();
        try {
            for (UserEntity userEntity : userEntityList) {
                String chatId = userEntity.getChatId().toString();
                String text = "Новая заявка  \n----------------------------------------\n Номер квартиры: " + recordData.getFlatNumber() + "\n Номер телефона: " + recordData.getPhoneNumber().replaceAll("\\+", "")
                        + "\n Марка автомобиля: " + recordData.getCarMark() + "\n Номер автомобиля: " + recordData.getCarNumber();
                RestTemplate restTemplate = new RestTemplate();
                final String baseUrl = "https://api.telegram.org/bot" + apiToken + "/sendMessage?chat_id=" + chatId + "&text=" + text;
                log.warn("baseURL = " + baseUrl);
                URI uri = new URI(baseUrl);

                ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);
                log.warn("URI = " + uri.toString());
                Thread.sleep(TimeUnit.SECONDS.toMillis(1));
            }
        } catch (InterruptedException | URISyntaxException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
