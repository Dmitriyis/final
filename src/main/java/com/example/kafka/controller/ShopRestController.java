package com.example.kafka.controller;

import com.example.kafka.dto.ShopDtoKafka;
import com.example.kafka.service.producer.ShopProducerService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShopRestController {

    private final ShopProducerService shopProducerService;

    public ShopRestController(ShopProducerService shopProducerService) {
        this.shopProducerService = shopProducerService;
    }

    @PostMapping("create-shop")
    public String createTopicOne(@RequestBody ShopDtoKafka shopDtoKafka) {
        shopProducerService.sendMessage(shopDtoKafka);
        return "Ok!";
    }
}
