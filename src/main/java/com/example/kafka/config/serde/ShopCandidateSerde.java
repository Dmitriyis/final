package com.example.kafka.config.serde;

import com.example.kafka.dto.ShopCandidateDtoKafka;
import io.confluent.kafka.serializers.json.KafkaJsonSchemaDeserializer;
import io.confluent.kafka.serializers.json.KafkaJsonSchemaSerializer;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;

import java.util.HashMap;
import java.util.Map;

public class ShopCandidateSerde implements Serde<ShopCandidateDtoKafka> {
    private final Serde<ShopCandidateDtoKafka> inner;

    public ShopCandidateSerde() {
        KafkaJsonSchemaSerializer<ShopCandidateDtoKafka> serializer = new KafkaJsonSchemaSerializer<>();
        KafkaJsonSchemaDeserializer<ShopCandidateDtoKafka> deserializer = new KafkaJsonSchemaDeserializer<>();

        Map<String, Object> serdeConfig = new HashMap<>();
        serdeConfig.put("schema.registry.url", "http://schema-registry:8081");
        serdeConfig.put("use.latest.version", true);
        serdeConfig.put("json.value.type", ShopCandidateDtoKafka.class);
        serializer.configure(serdeConfig, false);
        deserializer.configure(serdeConfig, false);

        this.inner = Serdes.serdeFrom(serializer, deserializer);
    }

    @Override
    public Serializer<ShopCandidateDtoKafka> serializer() {
        return inner.serializer();
    }

    @Override
    public Deserializer<ShopCandidateDtoKafka> deserializer() {
        return inner.deserializer();
    }
}
