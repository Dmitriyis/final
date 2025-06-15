package com.example.kafka.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ShopDto {

    @JsonProperty(value = "shop_id")
    private String shopId;
    @JsonProperty(value = "name")
    private String name;
    @JsonProperty(value = "description")
    private String description;
    @JsonProperty(value = "price")
    private Price price;
    @JsonProperty(value = "category")
    private String category;
    @JsonProperty(value = "brand")
    private String brand;
    @JsonProperty(value = "stock")
    private Stock stock;
    @JsonProperty(value = "sku")
    private String sku;
    @JsonProperty(value = "tags")
    private List<String> tags;
    @JsonProperty(value = "images")
    private List<Image> images;
    @JsonProperty(value = "specifications")
    private Map<String, String> specifications;
    @JsonProperty(value = "created_at")
    private LocalDateTime createdAt;
    @JsonProperty(value = "updated_at")
    private LocalDateTime updatedAt;
    @JsonProperty(value = "index")
    private String index;
    @JsonProperty(value = "store_id")
    private String storeId;

    @JsonProperty("created_at")
    public String getCreatedAt() {
        return createdAt != null ? createdAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null;
    }

    @JsonProperty("updated_at")
    public String getUpdatedAt() {
        return updatedAt != null ? updatedAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null;
    }

    @Getter
    @Setter
    @Builder
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Price {
        @JsonProperty("amount")
        private Double amount;
        @JsonProperty("currency")
        private String currency;
    }

    @Getter
    @Setter
    @Builder
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Stock {
        @JsonProperty("available")
        private Integer available;
        @JsonProperty("reserved")
        private Integer reserved;
    }

    @Getter
    @Setter
    @Builder
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Image {
        @JsonProperty("url")
        private String url;
        @JsonProperty("alt")
        private String alt;
    }
}
