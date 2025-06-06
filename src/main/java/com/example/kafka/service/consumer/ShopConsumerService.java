package com.example.kafka.service.consumer;

import com.example.kafka.dto.ShopDtoKafka;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class ShopConsumerService {
    private static final Logger log = LoggerFactory.getLogger(ShopConsumerService.class);

    @KafkaListener(topics = "shop", groupId = "shop-consumer-group", containerFactory = "listenerConsumerShop")
    public void listenStringMessage(@Payload ShopDtoKafka shopDtoKafka) {
        log.info("Получено сообщение из топика shop. Message: " + shopDtoKafka);
    }
}
