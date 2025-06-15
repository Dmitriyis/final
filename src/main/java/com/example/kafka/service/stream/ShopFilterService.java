package com.example.kafka.service.stream;

import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class ShopFilterService {

    public ShopFilterService(@Qualifier("streamsBuilderFactoryBean") StreamsBuilder streamsBuilder) {
        filteredShop(streamsBuilder);
    }

    public void filteredShop(StreamsBuilder streamsBuilder) {
        SpecificAvroSerde<com.example.kafka.avro.ShopDto> shopEventSerde = new SpecificAvroSerde<>();
        Map<String, Object> serdeConfig = new HashMap<>();
        serdeConfig.put("schema.registry.url", "http://schema-registry:8081");
        shopEventSerde.configure(serdeConfig, false);


        KStream<String, com.example.kafka.avro.ShopDto> shopCandidateStream = streamsBuilder
                .stream("shop-candidate", Consumed.with(Serdes.String(), shopEventSerde));

        GlobalKTable<String, String> blockedShopsTable = streamsBuilder.globalTable(
                "shop-blocked",
                Consumed.with(Serdes.String(), Serdes.String())
        );

        KStream<String, com.example.kafka.avro.ShopDto> candidateStreamByName = shopCandidateStream
                .selectKey((key, shop) -> String.valueOf(shop.getName()));

        KStream<String, com.example.kafka.avro.ShopDto> filteredStream = candidateStreamByName
                .leftJoin(
                        blockedShopsTable,
                        (shopKey, shopValue) -> shopKey,
                        (shop, blockedValue) -> blockedValue == null ? shop : null
                )
                .filter((key, shop) -> shop != null)
                .selectKey((key, shop) -> String.valueOf(shop.getShopId()));

        filteredStream.to("shop", Produced.with(Serdes.String(), shopEventSerde));
    }
}


