#!/bin/bash

### KAFKA-CLUSTER-REQ

# user:schema-registry
docker exec -i kafka-1-reg bash -c "\
  kafka-acls --bootstrap-server kafka-1:9092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:schema-registry \
  --operation ALL \
  --topic _schemas"

docker exec -i kafka-1-reg bash -c "\
  kafka-acls --bootstrap-server kafka-1:9092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:schema-registry \
  --operation ALL \
  --group schema-registry"


# user:ui
docker exec -i kafka-1-reg bash -c "\
  kafka-acls --bootstrap-server kafka-1:9092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:ui \
  --operation READ \
  --topic '*'"


# user:connect-user
docker exec -i kafka-1-reg bash -c "\
  kafka-acls --bootstrap-server kafka-1:9092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:connect-user \
  --operation ALL \
  --topic _connect-offsets"

docker exec -i kafka-1-reg bash -c "\
  kafka-acls --bootstrap-server kafka-1:9092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:connect-user \
  --operation ALL \
  --topic _connect-status"

docker exec -i kafka-1-reg bash -c "\
  kafka-acls --bootstrap-server kafka-1:9092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:connect-user \
  --operation ALL \
  --topic _connect-configs"

docker exec -i kafka-1-reg bash -c "\
  kafka-acls --bootstrap-server kafka-1:9092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:connect-user \
  --operation ALL \
  --group kafka-connect-group"

docker exec -i kafka-1-reg bash -c "\
  kafka-acls --bootstrap-server kafka-1:9092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:connect-user \
  --operation DESCRIBE \
  --group kafka-connect-group"

docker exec -i kafka-1-reg bash -c "\
  kafka-acls --bootstrap-server kafka-1:9092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:connect-user \
  --operation DESCRIBE \
  --topic shop"

docker exec -i kafka-1-reg bash -c "\
  kafka-acls --bootstrap-server kafka-1:9092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:connect-user \
  --operation ALL \
  --topic shop"

docker exec -i kafka-1-reg bash -c "\
  kafka-acls --bootstrap-server kafka-1:9092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:connect-user \
  --operation DESCRIBE \
  --group connect-elasticsearch-sink"

docker exec -i kafka-1-reg bash -c "\
  kafka-acls --bootstrap-server kafka-1:9092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:connect-user \
  --operation ALL \
  --group connect-elasticsearch-sink"


# user:shop
docker exec -i kafka-1-reg bash -c "\
  kafka-acls --bootstrap-server kafka-1:9092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:shop \
  --operation DESCRIBE \
  --group shop-consumer-group"

docker exec -i kafka-1-reg bash -c "\
  kafka-acls --bootstrap-server kafka-1:9092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:shop \
  --operation ALL \
  --topic shop"

docker exec -i kafka-1-reg bash -c "\
  kafka-acls --bootstrap-server kafka-1:9092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:shop \
  --operation ALL \
  --group shop-consumer-group"

docker exec -i kafka-1-reg bash -c "\
  kafka-acls --bootstrap-server kafka-1:9092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:shop \
  --operation DESCRIBE \
  --topic shop-candidate"

docker exec -i kafka-1-reg bash -c "\
  kafka-acls --bootstrap-server kafka-1:9092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:shop \
  --operation ALL \
  --topic shop-candidate"

docker exec -i kafka-1-reg bash -c "\
  kafka-acls --bootstrap-server kafka-1:9092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:shop \
  --operation DESCRIBE \
  --topic shop-blocked"

docker exec -i kafka-1-reg bash -c "\
  kafka-acls --bootstrap-server kafka-1:9092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:shop \
  --operation ALL \
  --topic shop-blocked"

docker exec -i kafka-1-reg bash -c "\
  kafka-acls --bootstrap-server kafka-1:9092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:shop \
  --operation All \
  --group app"

docker exec -i kafka-1-reg bash -c "\
  kafka-acls --bootstrap-server kafka-1:9092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:shop \
  --operation DESCRIBE \
  --group shop-consumer-group-mirror"

docker exec -i kafka-1-reg bash -c "\
  kafka-acls --bootstrap-server kafka-1:9092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:shop \
  --operation All \
  --group shop-consumer-group-mirror"

docker exec -i kafka-1-reg bash -c "\
  kafka-acls --bootstrap-server kafka-1:9092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:shop \
  --operation DESCRIBE \
  --topic statistic-client"

docker exec -i kafka-1-reg bash -c "\
  kafka-acls --bootstrap-server kafka-1:9092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:shop \
  --operation ALL \
  --topic statistic-client"


# user:app
docker exec -i kafka-1-reg bash -c "\
  kafka-acls --bootstrap-server kafka-1:9092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:app \
  --operation All \
  --operation Describe \
  --topic blocked-shop"

docker exec -i kafka-1-reg bash -c "\
  kafka-acls --bootstrap-server kafka-1:9092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:app \
  --operation All \
  --topic shop"

docker exec -i kafka-1-reg bash -c "\
  kafka-acls --bootstrap-server kafka-1:9092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:app \
  --operation Read \
  --operation Write \
  --operation Create \
  --topic app- \
  --resource-pattern-type PREFIXED"

docker exec -i kafka-1-reg bash -c "\
  kafka-acls --bootstrap-server kafka-1:9092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:app \
  --operation All \
  --group app"

docker exec -i kafka-1-reg bash -c "\
  kafka-acls --bootstrap-server kafka-1:9092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:app \
  --operation Read \
  --operation Describe \
  --topic blocked-shop"

docker exec -i kafka-1-reg bash -c "\
  kafka-acls --bootstrap-server kafka-1:9092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:app \
  --operation Write \
  --operation Create \
  --topic shop"

docker exec -i kafka-1-reg bash -c "\
  kafka-acls --bootstrap-server kafka-1:9092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:app \
  --operation Read \
  --operation Write \
  --operation Create \
  --topic app- \
  --resource-pattern-type PREFIXED"

docker exec -i kafka-1-reg bash -c "\
  kafka-acls --bootstrap-server kafka-1:9092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:app \
  --operation All \
  --group app"




### KAFKA-CLUSTER-DESTINATION

docker exec -i kafka-1-destination bash -c "\
  kafka-topics --create --bootstrap-server kafka-1-destination:7092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --replication-factor 3 \
  --partitions 3 \
  --topic shop \
  --config cleanup.policy=delete \
  --config min.insync.replicas=2 \
  --config retention.ms=604800000 \
  --config segment.bytes=1073741824"

docker exec -i kafka-1-destination bash -c "\
  kafka-topics --create --bootstrap-server kafka-1-destination:7092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --replication-factor 3 \
  --partitions 3 \
  --topic statistic-client \
  --config cleanup.policy=delete \
  --config min.insync.replicas=2 \
  --config retention.ms=604800000 \
  --config segment.bytes=1073741824"

docker exec -i kafka-1-destination bash -c "\
  kafka-topics --create --bootstrap-server kafka-1-destination:7092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --replication-factor 3 \
  --partitions 3 \
  --topic client-recommendations \
  --config cleanup.policy=delete \
  --config min.insync.replicas=2 \
  --config retention.ms=604800000 \
  --config segment.bytes=1073741824"

# user:schema-registry
docker exec -i kafka-1-destination bash -c "\
  kafka-acls --bootstrap-server kafka-1-destination:7092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:schema-registry \
  --operation ALL \
  --topic _schemas"

docker exec -i kafka-1-destination bash -c "\
  kafka-acls --bootstrap-server kafka-1-destination:7092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:schema-registry \
  --operation ALL \
  --group schema-registry"

# user:ui
docker exec -i kafka-1-destination bash -c "\
  kafka-acls --bootstrap-server kafka-1-destination:7092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:ui \
  --operation READ \
  --topic '*'"

# user:shop
docker exec -i kafka-1-destination bash -c "\
  kafka-acls --bootstrap-server kafka-1-destination:7092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:shop \
  --operation DESCRIBE \
  --group shop-consumer-group"

docker exec -i kafka-1-destination bash -c "\
  kafka-acls --bootstrap-server kafka-1-destination:7092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:shop \
  --operation ALL \
  --group shop-consumer-group"

docker exec -i kafka-1-destination bash -c "\
  kafka-acls --bootstrap-server kafka-1-destination:7092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:shop \
  --operation ALL \
  --topic shop"

docker exec -i kafka-1-destination bash -c "\
  kafka-acls --bootstrap-server kafka-1:9092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:shop \
  --operation DESCRIBE \
  --group shop-consumer-group-mirror"

docker exec -i kafka-1-destination bash -c "\
  kafka-acls --bootstrap-server kafka-1:9092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:shop \
  --operation ALL \
  --group shop-consumer-group-mirror"

docker exec -i kafka-1-destination bash -c "\
  kafka-acls --bootstrap-server kafka-1-destination:7092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:shop \
  --operation DESCRIBE \
  --topic statistic-client"

docker exec -i kafka-1-destination bash -c "\
  kafka-acls --bootstrap-server kafka-1-destination:7092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:shop \
  --operation ALL \
  --topic statistic-client"

docker exec -i kafka-1-destination bash -c "\
  kafka-acls --bootstrap-server kafka-1-destination:7092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:connect-user \
  --operation ALL \
  --topic _connect-offsets"

docker exec -i kafka-1-destination bash -c "\
  kafka-acls --bootstrap-server kafka-1-destination:7092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:connect-user \
  --operation ALL \
  --topic _connect-status"

docker exec -i kafka-1-destination bash -c "\
  kafka-acls --bootstrap-server kafka-1-destination:7092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:connect-user \
  --operation ALL \
  --topic _connect-configs"

docker exec -i kafka-1-destination bash -c "\
  kafka-acls --bootstrap-server kafka-1-destination:7092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:connect-user \
  --operation ALL \
  --topic statistic-client"

docker exec -i kafka-1-destination bash -c "\
  kafka-acls --bootstrap-server kafka-1-destination:7092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:connect-user \
  --operation DESCRIBE \
  --group connect-hdfs-sink"

docker exec -i kafka-1-destination bash -c "\
  kafka-acls --bootstrap-server kafka-1-destination:7092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:connect-user \
  --operation ALL \
  --group connect-hdfs-sink"

docker exec -i kafka-1-destination bash -c "\
  kafka-acls --bootstrap-server kafka-1-destination:7092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:connect-user \
  --operation DESCRIBE \
  --group kafka-connect-group"

docker exec -i kafka-1-destination bash -c "\
  kafka-acls --bootstrap-server kafka-1-destination:7092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:connect-user \
  --operation ALL \
  --group kafka-connect-group"

docker exec -i kafka-1-destination bash -c "\
  kafka-acls --bootstrap-server kafka-1-destination:7092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:shop \
  --operation DESCRIBE \
  --group hadoop-spark-consumer-group"

docker exec -i kafka-1-destination bash -c "\
  kafka-acls --bootstrap-server kafka-1-destination:7092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:shop \
  --operation All \
  --group hadoop-spark-consumer-group"

docker exec -i kafka-1-destination bash -c "\
  kafka-acls --bootstrap-server kafka-1-destination:7092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:shop \
  --operation ALL \
  --topic statistic-client"

docker exec -i kafka-1-destination bash -c "\
  kafka-acls --bootstrap-server kafka-1-destination:7092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:shop \
  --operation DESCRIBE \
  --topic client-recommendations"

docker exec -i kafka-1-destination bash -c "\
  kafka-acls --bootstrap-server kafka-1-destination:7092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:shop \
  --operation ALL \
  --topic client-recommendations"

sleep 3 && docker restart app
sleep 2 && docker restart mirror-maker