package com.example.kafka.converter;


import com.example.kafka.dto.StatisticDto;

public class StatisticConverter {

    public static com.example.kafka.avro.StatisticDto toStatisticDtoAvro(StatisticDto statisticDto) {
        return com.example.kafka.avro.StatisticDto.newBuilder()
                .setClientId(statisticDto.getClientId())
                .setCategory(statisticDto.getCategory())
                .build();
    }

    public static StatisticDto toStatisticDto(com.example.kafka.avro.StatisticDto statisticDtoAvro) {
        return StatisticDto.builder()
                .clientId(String.valueOf(statisticDtoAvro.getClientId()))
                .category(String.valueOf(statisticDtoAvro.getCategory()))
                .build();
    }
}
