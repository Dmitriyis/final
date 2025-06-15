package com.example.kafka.converter;


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ShopConverter {

    public static com.example.kafka.avro.ShopDto toAvro(com.example.kafka.dto.ShopDto dto) {
        if (dto == null) {
            return null;
        }

        com.example.kafka.avro.ShopDto avro = new com.example.kafka.avro.ShopDto();

        avro.setShopId(dto.getShopId());
        avro.setName(dto.getName());

        avro.setDescription(dto.getDescription() != null ? dto.getDescription() : null);

        // Price
        if (dto.getPrice() != null) {
            com.example.kafka.avro.Price price = new com.example.kafka.avro.Price();
            price.setAmount(dto.getPrice().getAmount() != null ? dto.getPrice().getAmount() : 0.0);
            price.setCurrency(dto.getPrice().getCurrency());
            avro.setPrice(price);
        } else {
            avro.setPrice(null);
        }

        avro.setCategory(dto.getCategory());
        avro.setBrand(dto.getBrand());

        // Stock
        if (dto.getStock() != null) {
            com.example.kafka.avro.Stock stock = new com.example.kafka.avro.Stock();
            stock.setAvailable(dto.getStock().getAvailable() != null ? dto.getStock().getAvailable() : 0);
            stock.setReserved(dto.getStock().getReserved() != null ? dto.getStock().getReserved() : 0);
            avro.setStock(stock);
        } else {
            avro.setStock(null);
        }

        avro.setSku(dto.getSku());

        List<java.lang.CharSequence> tags = null;
        if (dto.getTags() != null) {
            tags = dto.getTags().stream()
                    .map(s -> (java.lang.CharSequence) s)
                    .collect(Collectors.toList());
        }
        avro.setTags(tags);

        List<com.example.kafka.avro.Image> images = null;
        if (dto.getImages() != null) {
            images = dto.getImages().stream()
                    .map(ShopConverter::toAvroImage)
                    .collect(Collectors.toList());
        }
        avro.setImages(images);

        Map<java.lang.CharSequence, java.lang.CharSequence> specifications = null;
        if (dto.getSpecifications() != null) {
            specifications = dto.getSpecifications().entrySet().stream()
                    .collect(Collectors.toMap(
                            e -> (java.lang.CharSequence) e.getKey(),
                            e -> (java.lang.CharSequence) e.getValue()
                    ));
        }
        avro.setSpecifications(specifications);

        avro.setCreatedAt(dto.getCreatedAt() != null ? dto.getCreatedAt() : null);

        avro.setUpdatedAt(dto.getUpdatedAt() != null ? dto.getUpdatedAt() : null);

        avro.setIndex(dto.getIndex());
        avro.setStoreId(dto.getStoreId());

        return avro;
    }

    private static com.example.kafka.avro.Image toAvroImage(com.example.kafka.dto.ShopDto.Image image) {
        if (image == null) {
            return null;
        }
        com.example.kafka.avro.Image avroImage = new com.example.kafka.avro.Image();
        avroImage.setUrl(image.getUrl());
        avroImage.setAlt(image.getAlt());
        return avroImage;
    }
}
