package com.example.kafka.controller;

import com.example.kafka.entity.ShopDocument;
import com.example.kafka.service.ShopService;
import com.example.kafka.service.producer.StatisticProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("shops/")
@RequiredArgsConstructor
public class ClientRestController {

    private final ShopService shopService;
    private final StatisticProducerService statisticProducerService;

    @GetMapping("/{name}/{clientId}")
    public List<ShopDocument> getShops(@PathVariable(name = "name") String name,
                                       @PathVariable(name = "clientId") String clientId) {
        List<ShopDocument> shops = shopService.findByName(name);
        statisticProducerService.sendStatisticClient(clientId, shops);

        return shops;
    }
}

