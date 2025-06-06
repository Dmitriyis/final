#!/bin/bash

# user:schema-registry
docker exec -i kafka-1 bash -c "\
  kafka-acls --bootstrap-server kafka-1:9092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:schema-registry \
  --operation ALL \
  --topic _schemas"

docker exec -i kafka-1 bash -c "\
  kafka-acls --bootstrap-server kafka-1:9092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:schema-registry \
  --operation ALL \
  --group schema-registry"


# user:ui
docker exec -i kafka-1 bash -c "\
  kafka-acls --bootstrap-server kafka-1:9092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:ui \
  --operation READ \
  --topic '*'"


# user:client
docker exec -i kafka-1 bash -c "\
  kafka-acls --bootstrap-server kafka-1:9092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:client \
  --operation DESCRIBE \
  --group client-consumer-group"

docker exec -i kafka-1 bash -c "\
  kafka-acls --bootstrap-server kafka-1:9092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:client \
  --operation READ \
  --group client-consumer-group"

docker exec -i kafka-1 bash -c "\
  kafka-acls --bootstrap-server kafka-1:9092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:client \
  --operation READ \
  --topic client"


# user:shop
docker exec -i kafka-1 bash -c "\
  kafka-acls --bootstrap-server kafka-1:9092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:shop \
  --operation DESCRIBE \
  --group shop-consumer-group"

docker exec -i kafka-1 bash -c "\
  kafka-acls --bootstrap-server kafka-1:9092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:shop \
  --operation READ \
  --topic shop"

docker exec -i kafka-1 bash -c "\
  kafka-acls --bootstrap-server kafka-1:9092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:shop \
  --operation WRITE \
  --topic shop"

docker exec -i kafka-1 bash -c "\
  kafka-acls --bootstrap-server kafka-1:9092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:shop \
  --operation READ \
  --group shop-consumer-group"

sleep 5 && docker restart app

