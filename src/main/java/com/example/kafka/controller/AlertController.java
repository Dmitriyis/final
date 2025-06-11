package com.example.kafka.controller;

import com.example.kafka.dto.AlertPrometheusDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class AlertController {

    @PostMapping("/api/alerts")
    public void alert(@RequestBody AlertPrometheusDto alertDto) {
        log.info("Пришло уведомление о сбое в кластере Kafka: " + alertDto.getCommonLabels().getCluster());
        log.info(alertDto.toString());
    }
}
