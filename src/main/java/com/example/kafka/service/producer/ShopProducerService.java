package com.example.kafka.service.producer;

import com.example.kafka.converter.ShopConverter;
import com.example.kafka.dto.ShopDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ShopProducerService {

    private static final Logger log = LoggerFactory.getLogger(ShopProducerService.class);

    public final KafkaTemplate<String, com.example.kafka.avro.ShopDto> shopCandidateKafkaTemplate;
    private final KafkaTemplate<String, String> shopBlockedKafkaTemplate;

    public ShopProducerService(KafkaTemplate<String, com.example.kafka.avro.ShopDto> shopCandidateKafkaTemplate,
                               KafkaTemplate<String, String> shopBlokedKafkaTemplate) {
        this.shopCandidateKafkaTemplate = shopCandidateKafkaTemplate;
        this.shopBlockedKafkaTemplate = shopBlokedKafkaTemplate;
    }

    public void sendShopCandidateInTopic(ShopDto shopDtoKafka) {
        try {
            log.info("Объкт в кафку: " + shopDtoKafka.toString());

            com.example.kafka.avro.ShopDto shopDtoAvro = ShopConverter.toAvro(shopDtoKafka);

            shopCandidateKafkaTemplate.send("shop-candidate", shopDtoKafka.getShopId(), shopDtoAvro).get();
        } catch (Exception ex) {
            log.debug("Ошибка отправки сообщения в топик [shop-candidate]. " + ex.getMessage());
            throw new RuntimeException("Ошибка отправки сообщения в топик [shop-candidate]. " + ex.getMessage());
        }

        log.info("Сообщение отправлено в топик [shop-candidate]. Message: " + shopDtoKafka);
    }

    public void sendShopBlockedInTopic(String shopName) {
        try {
            log.info("Объкт в кафку: " + shopName.toString());
            shopBlockedKafkaTemplate.send("shop-blocked", shopName, shopName).get();
        } catch (Exception ex) {
            log.debug("Ошибка отправки сообщения в топик [shop-blocked]. " + ex.getMessage());
            throw new RuntimeException("Ошибка отправки сообщения в топик [shop-blocked]. " + ex.getMessage());
        }
        log.info("Сообщение отправлено в топик [shop-blocked]. Message: " + shopName);
    }
}
