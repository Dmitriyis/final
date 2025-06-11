package com.example.kafka.controller;

import com.example.kafka.dto.ShopCandidateDtoKafka;
import com.example.kafka.service.producer.ShopProducerService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShopRestController {

    private final ShopProducerService shopProducerService;

    public ShopRestController(ShopProducerService shopProducerService) {
        this.shopProducerService = shopProducerService;
    }

    @PostMapping("create-shop-candidate")
    public String createShop(@RequestBody ShopCandidateDtoKafka shopCandidateDtoKafka) {
        shopProducerService.sendShopCandidateInTopic(shopCandidateDtoKafka);
        return "Ok!";
    }

    @PostMapping("blocked-shop/{shopId}/{shopName}")
    public String blockedShop(@PathVariable(name = "shopName") String shopName,
                              @PathVariable(name = "shopId") String shopId) {
        shopProducerService.sendShopBlockedInTopic(shopId, shopName);
        return "Ok!";
    }
}
