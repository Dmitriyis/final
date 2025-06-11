package com.example.kafka.service.stream;

import com.example.kafka.config.serde.ShopCandidateSerde;
import com.example.kafka.dto.ShopCandidateDtoKafka;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.processor.api.Processor;
import org.apache.kafka.streams.processor.api.ProcessorContext;
import org.apache.kafka.streams.processor.api.Record;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;

@Service
public class ShopFilterService {

    public ShopFilterService(StreamsBuilder streamsBuilder) {
        filteredShop(streamsBuilder);
    }

    public void filteredShop(StreamsBuilder streamsBuilder) {

        streamsBuilder.stream("shop-blocked", Consumed.with(Serdes.String(), Serdes.String()))
                .process(() -> new Processor<String, String, String, String>() {
                    private KeyValueStore<String, LinkedHashSet<String>> store;

                    @Override
                    public void init(ProcessorContext<String, String> context) {
                        this.store = context.getStateStore("blocked-shop-store");
                    }

                    @Override
                    public void process(Record<String, String> record) {
                        String shopId = record.key();
                        String shopName = record.value();
                        LinkedHashSet<String> shopBlockedStore = store.get(shopId);
                        if (shopBlockedStore == null) {
                            shopBlockedStore = new LinkedHashSet<String>();
                        }
                        shopBlockedStore.add(shopName);
                        store.put(shopId, shopBlockedStore);
                    }
                }, "blocked-shop-store");

        ShopCandidateSerde shopCandidateSerde = new ShopCandidateSerde();
        KStream<String, ShopCandidateDtoKafka> shopCandidateStream = streamsBuilder
                .stream("shop-candidate", Consumed.with(Serdes.String(), shopCandidateSerde))
                .process(() -> new Processor<String, ShopCandidateDtoKafka, String, ShopCandidateDtoKafka>() {
                    private KeyValueStore<String, LinkedHashSet<String>> shopBlockedStores;
                    private ProcessorContext<String, ShopCandidateDtoKafka> context;

                    @Override
                    public void init(ProcessorContext<String, ShopCandidateDtoKafka> context) {
                        this.context = context;
                        this.shopBlockedStores = context.getStateStore("blocked-shop-store");
                    }

                    @Override
                    public void process(Record<String, ShopCandidateDtoKafka> record) {
                        ShopCandidateDtoKafka shopCandidate = record.value();
                        LinkedHashSet<String> shopBlocked = shopBlockedStores.get(shopCandidate.getShopId());

                        if (shopBlocked == null || !shopBlocked.contains(shopCandidate.getName())) {
                            context.forward(record);
                        }
                    }
                }, "blocked-shop-store");

        shopCandidateStream.to("shop", Produced.with(Serdes.String(), shopCandidateSerde));
    }
}
