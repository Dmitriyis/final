package com.example.kafka.config;


import io.confluent.kafka.schemaregistry.client.CachedSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.rest.exceptions.RestClientException;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import jakarta.annotation.PostConstruct;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.StreamsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.util.backoff.FixedBackOff;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static com.example.kafka.util.kafka.TopicName.SHOP;
import static com.example.kafka.util.kafka.TopicName.SHOP_BLOCKED;
import static com.example.kafka.util.kafka.TopicName.SHOP_CANDIDATE;
import static com.example.kafka.util.kafka.TopicName.STATISTIC_CLIENT;

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
        return TopicBuilder.name(SHOP_CANDIDATE)
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
        return TopicBuilder.name(SHOP_BLOCKED)
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
        return TopicBuilder.name(SHOP)
                .replicas(3)
                .partitions(3)
                .config("cleanup.policy", "delete")
                .config("min.insync.replicas", "2")
                .config("retention.ms", "604800000")
                .config("segment.bytes", "1073741824")
                .build();
    }

    @Bean
    public NewTopic newTopicStatisticClient() {
        return TopicBuilder.name(STATISTIC_CLIENT)
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
    public KafkaTemplate<String, com.example.kafka.avro.ShopDto> kafkaTemplateTopicShop(KafkaProperties kafkaProperties) {
        HashMap<String, Object> configProps = new HashMap<>(kafkaProperties.buildProducerProperties());
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        configProps.put(ProducerConfig.ACKS_CONFIG, "all");
        configProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);

        aclConf(configProps, "shop", saslPassword);
        baseConfig(configProps);

        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(configProps));
    }


    @Bean
    public KafkaTemplate<String, String> kafkaTemplateBlockedShop(KafkaProperties kafkaProperties) {
        HashMap<String, Object> configProps = new HashMap<>(kafkaProperties.buildProducerProperties());
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.ACKS_CONFIG, "all");
        configProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);

        aclConf(configProps, "shop", saslPassword);
        baseConfig(configProps);

        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(configProps));
    }

    @Bean(name = "kafkaTemplateClientStatisticBrokerReq")
    public KafkaTemplate<String, com.example.kafka.avro.StatisticDto> kafkaTemplateClientStatisticBrokerReq(KafkaProperties kafkaProperties) {
        HashMap<String, Object> configProps = new HashMap<>(kafkaProperties.buildProducerProperties());
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        configProps.put(ProducerConfig.ACKS_CONFIG, "all");

        aclConf(configProps, "shop", saslPassword);
        baseConfig(configProps);

        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(configProps));
    }

    @Bean(name = "kafkaTemplateClientStatisticBrokerDestination")
    public KafkaTemplate<String, com.example.kafka.avro.StatisticDto> kafkaTemplateClientStatisticBrokerDestination(KafkaProperties kafkaProperties) {
        HashMap<String, Object> configProps = new HashMap<>(kafkaProperties.buildProducerProperties());
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka-1-destination:7093,kafka-2-destination:7095,kafka-3-destination:7097");
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        configProps.put(ProducerConfig.ACKS_CONFIG, "all");

        aclConf(configProps, "shop", saslPassword);
        baseConfig(configProps);

        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(configProps));
    }

    // Consumer

//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<String, com.example.kafka.avro.ShopDto> listenerConsumerShop(KafkaProperties kafkaProperties, DefaultErrorHandler errorHandler) {
//        HashMap<String, Object> configProps = new HashMap<>(kafkaProperties.buildConsumerProperties());
//        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
//        configProps.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, KafkaAvroDeserializer.class);
//        configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
//        configProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
//        configProps.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 20);
//        configProps.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "10000");
//        configProps.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, "3000");
//        configProps.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, 600000);
//        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "shop-consumer-group");
//
//        configProps.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_SSL");
//        configProps.put("specific.avro.reader", true);
//
//        baseConfig(configProps);
//        aclConf(configProps, "shop", saslPassword);
//
//        ConcurrentKafkaListenerContainerFactory<String, com.example.kafka.avro.ShopDto> listenerConsumerShop = new ConcurrentKafkaListenerContainerFactory<>();
//        listenerConsumerShop.setConsumerFactory(new DefaultKafkaConsumerFactory<>(configProps));
//        listenerConsumerShop.setCommonErrorHandler(errorHandler);
//        return listenerConsumerShop;
//    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, com.example.kafka.avro.StatisticDto> listenerStatisticClient(KafkaProperties kafkaProperties, DefaultErrorHandler errorHandler) {

        HashMap<String, Object> configProps = new HashMap<>(kafkaProperties.buildConsumerProperties());
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka-1-destination:7093,kafka-2-destination:7095,kafka-3-destination:7097");
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        configProps.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, KafkaAvroDeserializer.class);
        configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        configProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        configProps.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, 1880);

        configProps.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, 1880);
        configProps.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 20);
        configProps.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, 600000);

        aclConf(configProps, "shop", saslPassword);
        baseConfig(configProps);
        configProps.put("specific.avro.reader", true);

        ConcurrentKafkaListenerContainerFactory<String, com.example.kafka.avro.StatisticDto> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(configProps));
        factory.setCommonErrorHandler(errorHandler);
        factory.setBatchListener(true);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        factory.getContainerProperties().setIdleBetweenPolls(5000);

        return factory;
    }

    // Stream

    @Bean(name = "streamsBuilderFactoryBean")
    public StreamsBuilderFactoryBean streamsBuilderFactoryBean() {
        HashMap<String, Object> properties = new HashMap<>();
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "app");
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        aclConf(properties, "shop", saslPassword);
        baseConfig(properties);
        StreamsBuilderFactoryBean builderFactoryBean = new StreamsBuilderFactoryBean(new KafkaStreamsConfiguration(properties));
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
        baseConfig(adminProps);
        aclConf(adminProps, "admin", "admin-secret");
        return new KafkaAdmin(adminProps);
    }

    @PostConstruct
    public void init() {
        CachedSchemaRegistryClient cachedSchemaRegistryClient = new CachedSchemaRegistryClient("http://schema-registry:8081", 100);

        String avroSchemaStatistic;
        String avroSchemaShop;
        String avroSchemaRecommendation;
        try {
            avroSchemaStatistic = Files.readString(Paths.get("./kafka-schema/statistic.avsc"));
            avroSchemaShop = Files.readString(Paths.get("./kafka-schema/shop.avsc"));
            avroSchemaRecommendation = Files.readString(Paths.get("./kafka-schema/recommendation.avsc"));
        } catch (IOException e) {
            log.error("Ошибка при чтении схемы из файла: " + e.getMessage());
            throw new RuntimeException(e);
        }

        log.info("Регистрация схемы...");
        try {
            org.apache.avro.Schema schemaStatistic = new org.apache.avro.Schema.Parser().parse(avroSchemaStatistic);
            org.apache.avro.Schema schemaShop = new org.apache.avro.Schema.Parser().parse(avroSchemaShop);
            org.apache.avro.Schema schemaRecommendation = new org.apache.avro.Schema.Parser().parse(avroSchemaRecommendation);
            cachedSchemaRegistryClient.register("statistic-client-value", schemaStatistic);
            cachedSchemaRegistryClient.register("shop-candidate-value", schemaShop);
            cachedSchemaRegistryClient.register("client-recommendations-value", schemaRecommendation);
        } catch (IOException | RestClientException ex) {
            log.error("Ошибка регистрация схемы: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
        log.info("Схема была успешно зарегистрирована. [statistic-client-value]: ");
        log.info("Схема была успешно зарегистрирована. [shop-candidate-value]: ");
        log.info("Схема была успешно зарегистрирована. [client-recommendations-value]: ");
    }


    private void baseConfig(Map<String, Object> configProps) {
        configProps.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_SSL");
        configProps.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, truststoreLocation);
        configProps.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, truststorePassword);
        configProps.put(SslConfigs.SSL_ENDPOINT_IDENTIFICATION_ALGORITHM_CONFIG, "");

        configProps.put("schema.registry.url", "http://schema-registry:8081");
        configProps.put("auto.register.schemas", false);
        configProps.put("use.latest.version", true);
    }

    private void aclConf(Map<String, Object> configProps, String user, String password) {
        configProps.put(SaslConfigs.SASL_MECHANISM, "PLAIN");
        configProps.put(SaslConfigs.SASL_JAAS_CONFIG, "org.apache.kafka.common.security.plain.PlainLoginModule required " +
                "username=\"" + user + "\" " +
                "password=\"" + password + "\";");
    }
}

