package com.example.kafka.controller;

import com.example.kafka.dto.ShopDto;
import com.example.kafka.service.producer.ShopProducerService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("shops/")
public class ShopRestController {

    private final ShopProducerService shopProducerService;

    public ShopRestController(ShopProducerService shopProducerService) {
        this.shopProducerService = shopProducerService;
    }

    @PostMapping("create-candidate")
    public String createShop(@RequestBody ShopDto shopCandidateDtoKafka) {
        shopProducerService.sendShopCandidateInTopic(shopCandidateDtoKafka);
        return "Ok!";
    }

    @PostMapping("blocked/{shopName}")
    public String blockedShop(@PathVariable(name = "shopName") String shopName) {
        shopProducerService.sendShopBlockedInTopic(shopName);
        return "Ok!";
    }
}
