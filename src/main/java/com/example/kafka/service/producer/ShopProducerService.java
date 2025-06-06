package com.example.kafka.service.producer;

import com.example.kafka.dto.ShopDtoKafka;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ShopProducerService {

    private static final Logger log = LoggerFactory.getLogger(ShopProducerService.class);

    public final KafkaTemplate<String, ShopDtoKafka> kafkaTemplate;

    public ShopProducerService(KafkaTemplate<String, ShopDtoKafka> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(ShopDtoKafka shopDtoKafka) {
        try {
            kafkaTemplate.send("shop", shopDtoKafka).get();
        } catch (Exception ex) {
            log.debug("Ошибка отправки сообщения в топик [shop]. " + ex.getMessage());
            throw new RuntimeException("Ошибка отправки сообщения в топик [shop]. " + ex.getMessage());
        }

        log.info("Сообщение отправлено в топик [shop]. Message: " + shopDtoKafka);
    }
}
