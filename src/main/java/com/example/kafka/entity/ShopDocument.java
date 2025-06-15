package com.example.kafka.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;
import org.springframework.data.elasticsearch.core.join.JoinField;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "shop")
public class ShopDocument {

    @Id
    private String id;

    @Field(type = FieldType.Text, name = "shop_id")
    private String shopId;

    @Field(type = FieldType.Text)
    private String name;

    @Field(type = FieldType.Text)
    private String description;

    @Field(type = FieldType.Nested)
    private Price price;

    @Field(type = FieldType.Keyword)
    private String category;

    @Field(type = FieldType.Keyword)
    private String brand;

    @Field(type = FieldType.Nested)
    private Stock stock;

    @Field(type = FieldType.Keyword)
    private String sku;

    @Field(type = FieldType.Keyword)
    private List<String> tags;

    @Field(type = FieldType.Nested)
    private List<Image> images;

    @Field(type = FieldType.Object)
    private Map<String, String> specifications;

    @Field(type = FieldType.Date, format = {DateFormat.date_hour_minute_second, DateFormat.date_time}, name = "created_at")
    private LocalDateTime createdAt;

    @Field(type = FieldType.Date, format = {DateFormat.date_hour_minute_second, DateFormat.date_time}, name = "updated_at")
    private LocalDateTime updatedAt;

    @Field(type = FieldType.Keyword)
    private String index;

    @Field(type = FieldType.Text, name = "store_id")
    private String storeId;

    @Getter
    @Setter
    @Builder
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Price {
        @Field(type = FieldType.Double)
        private Double amount;

        @Field(type = FieldType.Keyword)
        private String currency;
    }

    @Getter
    @Setter
    @Builder
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Stock {
        @Field(type = FieldType.Integer)
        private Integer available;

        @Field(type = FieldType.Integer)
        private Integer reserved;
    }

    @Getter
    @Setter
    @Builder
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Image {
        @Field(type = FieldType.Text)
        private String url;

        @Field(type = FieldType.Text)
        private String alt;
    }
}