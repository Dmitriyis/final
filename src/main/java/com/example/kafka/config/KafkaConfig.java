package com.example.kafka.config;

import com.example.kafka.dto.ClientDtoKafka;
import com.example.kafka.dto.ShopCandidateDtoKafka;
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
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.state.Stores;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.config.KafkaStreamsInfrastructureCustomizer;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.util.backoff.FixedBackOff;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedHashSet;
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
    public NewTopic newTopicShopCandidate() {
        return TopicBuilder.name("shop-candidate")
                .replicas(3)
                .partitions(3)
                .config("cleanup.policy", "delete")
                .config("min.insync.replicas", "2")
                .config("retention.ms", "604800000")
                .config("segment.bytes", "1073741824")
                .build();
    }

    @Bean
    public NewTopic newTopicShopBlocked() {
        return TopicBuilder.name("shop-blocked")
                .replicas(3)
                .partitions(3)
                .config("cleanup.policy", "delete")
                .config("min.insync.replicas", "2")
                .config("retention.ms", "604800000")
                .config("segment.bytes", "1073741824")
                .build();
    }

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
    public KafkaTemplate<String, ShopCandidateDtoKafka> kafkaTemplateTopicShop(KafkaProperties kafkaProperties) {
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

    @Bean
    public KafkaTemplate<String, String> kafkaTemplateBlockedShop(KafkaProperties kafkaProperties) {
        HashMap<String, Object> configProps = new HashMap<>(kafkaProperties.buildProducerProperties());
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
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

        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(configProps));
    }

    // Consumer

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ShopCandidateDtoKafka> listenerConsumerShop(KafkaProperties kafkaProperties, DefaultErrorHandler errorHandler) {
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
        configProps.put(KafkaJsonSchemaDeserializerConfig.JSON_VALUE_TYPE, ShopCandidateDtoKafka.class);

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

        ConcurrentKafkaListenerContainerFactory<String, ShopCandidateDtoKafka> listenerConsumerShop = new ConcurrentKafkaListenerContainerFactory<>();
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

        configProps.put("schema.registry.url", "http://schema-registry:8081");
        configProps.put("use.latest.version", true);

        ConcurrentKafkaListenerContainerFactory<String, ClientDtoKafka> listenerConsumerClient = new ConcurrentKafkaListenerContainerFactory<>();
        listenerConsumerClient.setConsumerFactory(new DefaultKafkaConsumerFactory<>(configProps));

        return listenerConsumerClient;
    }

    // Stream

    @Bean
    public StreamsBuilderFactoryBean streamsBuilderFactoryBean() {
        HashMap<String, Object> properties = new HashMap<>();
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "app");
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put("schema.registry.url", "http://schema-registry:8081");
        properties.put("use.latest.version", true);

        properties.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_SSL");
        properties.put(SaslConfigs.SASL_MECHANISM, "PLAIN");
        properties.put(SaslConfigs.SASL_JAAS_CONFIG, "org.apache.kafka.common.security.plain.PlainLoginModule required " +
                "username=\"" + "shop" + "\" " +
                "password=\"" + saslPassword + "\";");

        properties.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, truststoreLocation);
        properties.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, truststorePassword);
        properties.put(SslConfigs.SSL_ENDPOINT_IDENTIFICATION_ALGORITHM_CONFIG, "");

        StreamsBuilderFactoryBean builderFactoryBean = new StreamsBuilderFactoryBean(new KafkaStreamsConfiguration(properties));

        builderFactoryBean.setInfrastructureCustomizer(new KafkaStreamsInfrastructureCustomizer() {
            @Override
            public void configureBuilder(StreamsBuilder builder) {
                builder.addStateStore(Stores.keyValueStoreBuilder(
                        Stores.persistentKeyValueStore("blocked-shop-store"),
                        Serdes.String(),
                        new JsonSerde<>(LinkedHashSet.class)
                ));
            }
        });
        return builderFactoryBean;
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

