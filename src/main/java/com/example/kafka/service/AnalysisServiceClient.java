package com.example.kafka.service;

import com.example.kafka.converter.StatisticConverter;
import com.example.kafka.dto.StatisticDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import scala.Tuple2;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.example.kafka.util.kafka.TopicName.CLIENT_RECOMMENDATIONS;

@Service
public class AnalysisServiceClient {

    @Value("${spring.hdfs.user}")
    private String hdfsUser;
    @Value("${spring.hdfs.uri}")
    private String hdfsUri;
    @Value("${spring.hdfs.path}")
    private String hdfsPath;
    private final ObjectMapper objectMapper;

    private final KafkaTemplate<String, com.example.kafka.avro.StatisticDto> kafkaTemplateStatisticDto;

    public AnalysisServiceClient(@Qualifier("kafkaTemplateClientStatisticBrokerDestination") KafkaTemplate<String, com.example.kafka.avro.StatisticDto> kafkaTemplateStatisticDto,
                                 ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.kafkaTemplateStatisticDto = kafkaTemplateStatisticDto;
    }

    @KafkaListener(topics = "statistic-client", groupId = "hadoop-spark-consumer-group", containerFactory = "listenerStatisticClient")
    public void listen(@Payload List<com.example.kafka.avro.StatisticDto> statisticDtosAvro, Acknowledgment acknowledgment) {
        writingTohdfs(statisticDtosAvro);
        acknowledgment.acknowledge();
        processWithSpark();
    }

    private void processWithSpark() {
            SparkConf sparkConf = new SparkConf()
                    .setAppName("KafkaHdfsSparkConsumer")
                    .setMaster("local[*]")
                    .set("spark.ui.enabled", "false")
                    .set("spark.metrics.conf", "false");

            try (JavaSparkContext sc = new JavaSparkContext(sparkConf)) {
                JavaRDD<String> rawData = sc.textFile(hdfsUri + hdfsPath)
                        .filter(str -> {
                            String cleaned = str.replaceAll("\\p{C}", "").trim();
                            boolean isValid = !cleaned.isEmpty() && cleaned.startsWith("{") && cleaned.endsWith("}");
                            System.out.println("[DEBUG] Проверка строки: " + cleaned.substring(0, Math.min(cleaned.length(), 50)) +
                                    " | valid=" + isValid);
                            return isValid;
                        });

                JavaRDD<StatisticDto> parsedData = rawData.mapPartitionsWithIndex((idx, iter) -> {
                    ObjectMapper mapper = new ObjectMapper();
                    List<StatisticDto> result = new ArrayList<>();

                    iter.forEachRemaining(json -> {
                        try {
                            StatisticDto dto = mapper.readValue(json, StatisticDto.class);
                            result.add(dto);
                        } catch (Exception e) {
                            System.out.println("[ERROR] Ошибка парсинга: " + json);
                        }
                    });
                    return result.iterator();
                }, true).cache();

                Map<String, String> clientTopCategories = parsedData
                        .mapToPair(dto -> new Tuple2<>(dto.getClientId(), dto.getCategory()))
                        .groupByKey()
                        .mapValues(iter -> {
                            Map<String, Long> counts = new HashMap<>();
                            iter.forEach(cat -> counts.merge(cat, 1L, Long::sum));
                            return counts.entrySet().stream()
                                    .max(Map.Entry.comparingByValue())
                                    .map(Map.Entry::getKey)
                                    .orElse("N/A");
                        })
                        .collectAsMap();
                System.out.println("clientTopCategories: " + clientTopCategories);
                clientTopCategories.forEach((client, cat) -> {
                    com.example.kafka.avro.StatisticDto statisticDtoAvro = com.example.kafka.avro.StatisticDto.newBuilder()
                            .setClientId(client)
                            .setCategory(cat)
                            .build();

                    kafkaTemplateStatisticDto.send(CLIENT_RECOMMENDATIONS, client, statisticDtoAvro);
                });

        } catch (Exception e) {
            throw e;
        }
    }

    private void writingTohdfs(List<com.example.kafka.avro.StatisticDto> statisticDtosAvro) {
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", hdfsUri);

        try (FileSystem hdfs = FileSystem.get(new URI(hdfsUri), conf, hdfsUser)) {
            for (com.example.kafka.avro.StatisticDto statisticDtoAvro : statisticDtosAvro) {

                StatisticDto statisticDto = StatisticConverter.toStatisticDto(statisticDtoAvro);
                String hdfsFilePath = hdfsPath + UUID.randomUUID();
                Path path = new Path(hdfsFilePath);

                String statisticFormatJson = objectMapper.writeValueAsString(statisticDto);

                try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(hdfs.create(path, true), StandardCharsets.UTF_8))) {
                    writer.write(statisticFormatJson);
                    writer.newLine();
                }
            }
        } catch (IOException | InterruptedException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}