package com.example.kafka.service.producer;

import com.example.kafka.avro.StatisticDto;
import com.example.kafka.entity.ShopDocument;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatisticProducerService {

    private final KafkaTemplate<String, com.example.kafka.avro.StatisticDto> kafkaTemplateStatisticDto;

    public StatisticProducerService(@Qualifier("kafkaTemplateClientStatisticBrokerReq") KafkaTemplate<String, StatisticDto> kafkaTemplateStatisticDto) {
        this.kafkaTemplateStatisticDto = kafkaTemplateStatisticDto;
    }

    public void sendStatisticClient(String clientId, List<ShopDocument> shops) {
        List<String> categorys = shops.stream().map(ShopDocument::getCategory).distinct().toList();

        categorys.forEach(category -> {
            com.example.kafka.avro.StatisticDto statisticDtoAvro = com.example.kafka.avro.StatisticDto
                    .newBuilder()
                    .setClientId(clientId)
                    .setCategory(category)
                    .build();


            kafkaTemplateStatisticDto.send("statistic-client", clientId, statisticDtoAvro);
        });
    }
}
