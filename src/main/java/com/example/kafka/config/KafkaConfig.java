package com.example.kafka.config;

import com.example.kafka.dto.ClientDtoKafka;
import com.example.kafka.dto.ShopDtoKafka;
import io.confluent.kafka.schemaregistry.client.CachedSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.rest.exceptions.RestClientException;
import io.confluent.kafka.schemaregistry.json.JsonSchema;
import io.confluent.kafka.serializers.json.KafkaJsonSchemaDeserializer;
import io.confluent.kafka.serializers.json.KafkaJsonSchemaDeserializerConfig;
import io.confluent.kafka.serializers.json.KafkaJsonSchemaSerializer;
import jakarta.annotation.PostConstruct;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.FixedBackOff;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {
    private static final Logger log = LoggerFactory.getLogger(KafkaConfig.class);

    @Value("${kafka.truststore.location:/etc/kafka/secrets/client.truststore}")
    private String truststoreLocation;

    @Value("${kafka.truststore.password:12345678}")
    private String truststorePassword;

    @Value("${kafka.sasl.password:12345678}")
    private String saslPassword;

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;


    // Topic
    @Bean
    public NewTopic newTopicShop() {
        return TopicBuilder.name("shop")
                .replicas(3)
                .partitions(3)
                .config("cleanup.policy", "delete")
                .config("min.insync.replicas", "2")
                .config("retention.ms", "604800000")
                .config("segment.bytes", "1073741824")
                .build();
    }

    @Bean
    public NewTopic newTopicClient() {
        return TopicBuilder.name("client")
                .replicas(3)
                .partitions(3)
                .config("cleanup.policy", "delete")
                .config("min.insync.replicas", "2")
                .config("retention.ms", "604800000")
                .config("segment.bytes", "1073741824")
                .build();
    }

    // Producer

    @Bean
    public KafkaTemplate<String, ShopDtoKafka> kafkaTemplateTopicShop(KafkaProperties kafkaProperties) {
        HashMap<String, Object> configProps = new HashMap<>(kafkaProperties.buildProducerProperties());
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaJsonSchemaSerializer.class);
        configProps.put(ProducerConfig.ACKS_CONFIG, "all");
        configProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);

        configProps.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_SSL");
        configProps.put(SaslConfigs.SASL_MECHANISM, "PLAIN");
        configProps.put(SaslConfigs.SASL_JAAS_CONFIG, "org.apache.kafka.common.security.plain.PlainLoginModule required " +
                "username=\"" + "shop" + "\" " +
                "password=\"" + saslPassword + "\";");

        configProps.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, truststoreLocation);
        configProps.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, truststorePassword);
        configProps.put(SslConfigs.SSL_ENDPOINT_IDENTIFICATION_ALGORITHM_CONFIG, "");

        configProps.put("schema.registry.url", "http://schema-registry:8081");
        configProps.put("use.latest.version", true);

        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(configProps));
    }

    // Consumer

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ShopDtoKafka> listenerConsumerShop(KafkaProperties kafkaProperties, DefaultErrorHandler errorHandler) {
        HashMap<String, Object> configProps = new HashMap<>(kafkaProperties.buildConsumerProperties());
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        configProps.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, KafkaJsonSchemaDeserializer.class);
        configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        configProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        configProps.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "10000");
        configProps.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, "3000");
        configProps.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, 200);
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "shop-consumer-group");
        configProps.put(KafkaJsonSchemaDeserializerConfig.JSON_VALUE_TYPE, ShopDtoKafka.class);

        configProps.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_SSL");
        configProps.put(SaslConfigs.SASL_MECHANISM, "PLAIN");
        configProps.put(SaslConfigs.SASL_JAAS_CONFIG, "org.apache.kafka.common.security.plain.PlainLoginModule required " +
                "username=\"" + "shop" + "\" " +
                "password=\"" + saslPassword + "\";");

        configProps.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, truststoreLocation);
        configProps.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, truststorePassword);
        configProps.put(SslConfigs.SSL_ENDPOINT_IDENTIFICATION_ALGORITHM_CONFIG, "");

        configProps.put("schema.registry.url", "http://schema-registry:8081");
        configProps.put("use.latest.version", true);

        ConcurrentKafkaListenerContainerFactory<String, ShopDtoKafka> listenerConsumerShop = new ConcurrentKafkaListenerContainerFactory<>();
        listenerConsumerShop.setConsumerFactory(new DefaultKafkaConsumerFactory<>(configProps));
        listenerConsumerShop.setCommonErrorHandler(errorHandler);
        return listenerConsumerShop;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ClientDtoKafka> listenerConsumerClient(KafkaProperties kafkaProperties) {
        HashMap<String, Object> configProps = new HashMap<>(kafkaProperties.buildConsumerProperties());
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        configProps.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);
        configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        configProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        configProps.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "10000");
        configProps.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, "3000");
        configProps.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, 200);
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "client-consumer-group");
        configProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "com.example.kafka.dto.ClientDtoKafka");
        configProps.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_SSL");
        configProps.put(SaslConfigs.SASL_MECHANISM, "PLAIN");
        configProps.put(SaslConfigs.SASL_JAAS_CONFIG, "org.apache.kafka.common.security.plain.PlainLoginModule required " +
                "username=\"" + "client" + "\" " +
                "password=\"" + saslPassword + "\";");

        configProps.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, truststoreLocation);
        configProps.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, truststorePassword);
        configProps.put(SslConfigs.SSL_ENDPOINT_IDENTIFICATION_ALGORITHM_CONFIG, "");

        ConcurrentKafkaListenerContainerFactory<String, ClientDtoKafka> listenerConsumerClient = new ConcurrentKafkaListenerContainerFactory<>();
        listenerConsumerClient.setConsumerFactory(new DefaultKafkaConsumerFactory<>(configProps));

        return listenerConsumerClient;
    }

    // Handler

    @Bean
    public DefaultErrorHandler kafkaErrorHandler() {
        DefaultErrorHandler handler = new DefaultErrorHandler(
                (record, exception) -> {
                    log.info("[ОШИБКА] Не удалось обработать сообщение: " +
                            "Топик=" + record.topic() +
                            ", Ключ=" + record.key() +
                            ", Значения=" + record.value() +
                            ", Ошибка=" + exception.getCause());
                },
                new FixedBackOff(2000L, 5)
        );

        handler.setAckAfterHandle(false);
        handler.addNotRetryableExceptions(Exception.class);

        return handler;
    }

    // KafkaAdmin

    @Bean
    public KafkaAdmin kafkaAdmin(KafkaProperties kafkaProperties) {
        Map<String, Object> adminProps = kafkaProperties.buildAdminProperties(null);
        adminProps.put("client.id", "admin-client");
        adminProps.put("bootstrap.servers", bootstrapServers);

        adminProps.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, truststoreLocation);
        adminProps.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, truststorePassword);
        adminProps.put(SslConfigs.SSL_ENDPOINT_IDENTIFICATION_ALGORITHM_CONFIG, "");

        adminProps.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_SSL");
        adminProps.put(SaslConfigs.SASL_MECHANISM, "PLAIN");
        adminProps.put(SaslConfigs.SASL_JAAS_CONFIG, "org.apache.kafka.common.security.plain.PlainLoginModule required " +
                "username=\"" + "admin" + "\" " +
                "password=\"" + "admin-secret" + "\";");

        return new KafkaAdmin(adminProps);
    }

    @PostConstruct
    public void init() {
        CachedSchemaRegistryClient cachedSchemaRegistryClient = new CachedSchemaRegistryClient("http://schema-registry:8081", 100);

        String jsonSchema;
        try {
            jsonSchema = Files.readString(Paths.get("./kafka-schema/shop-schema.json"));
        } catch (IOException e) {
            log.error("Ошибка при чтении схемы из файла: " + e.getMessage());
            throw new RuntimeException(e);
        }

        log.info("Регистрация схемы...");
        try {
            JsonSchema schema = new JsonSchema(jsonSchema);
            cachedSchemaRegistryClient.register("shop", schema);
        } catch (IOException | RestClientException ex) {
            log.info("Ошибка регистрация схемы. " + ex.getMessage());
            throw new RuntimeException(ex);
        }
        log.info("Схема была успешно зарегистрирована. [shop]: ");
    }
}

