package com.example.kafka.service.consumer;

import com.example.kafka.dto.ClientDtoKafka;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class ClientConsumerService {

    private static final Logger log = LoggerFactory.getLogger(ClientConsumerService.class);

    @KafkaListener(topics = "client", groupId = "client-consumer-group", containerFactory = "listenerConsumerClient")
    public void listenStringMessage(@Payload ClientDtoKafka clientDto) {
        log.info("Получено сообщение из топика client. Message: " + clientDto);
    }
}
