package com.example.kafka.service;

import com.example.kafka.entity.ShopDocument;
import com.example.kafka.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShopService {

    private final ShopRepository shopRepository;

    public List<ShopDocument> findByName(String name) {
        return shopRepository.findByName(name);
    }
}
