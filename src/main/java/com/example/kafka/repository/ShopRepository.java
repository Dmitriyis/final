package com.example.kafka.repository;

import com.example.kafka.entity.ShopDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopRepository extends ElasticsearchRepository<ShopDocument, String> {
    List<ShopDocument> findByName(String name);
}
