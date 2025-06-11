#!/bin/bash

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


# user:client
docker exec -i kafka-1-reg bash -c "\
  kafka-acls --bootstrap-server kafka-1:9092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:client \
  --operation DESCRIBE \
  --group client-consumer-group"

docker exec -i kafka-1-reg bash -c "\
  kafka-acls --bootstrap-server kafka-1:9092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:client \
  --operation READ \
  --group client-consumer-group"

docker exec -i kafka-1-reg bash -c "\
  kafka-acls --bootstrap-server kafka-1:9092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:client \
  --operation READ \
  --topic client"


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
  --operation READ \
  --topic shop"

docker exec -i kafka-1-reg bash -c "\
  kafka-acls --bootstrap-server kafka-1:9092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:shop \
  --operation WRITE \
  --topic shop"

docker exec -i kafka-1-reg bash -c "\
  kafka-acls --bootstrap-server kafka-1:9092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:shop \
  --operation READ \
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
  --operation READ \
  --topic shop-candidate"

docker exec -i kafka-1-reg bash -c "\
  kafka-acls --bootstrap-server kafka-1:9092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:shop \
  --operation WRITE \
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
  --operation READ \
  --topic shop-blocked"

docker exec -i kafka-1-reg bash -c "\
  kafka-acls --bootstrap-server kafka-1:9092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:shop \
  --operation WRITE \
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
  --topic app-blocked-shop-store-changelog"

docker exec -i kafka-1-reg bash -c "\
  kafka-acls --bootstrap-server kafka-1:9092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:shop \
  --operation All \
  --topic app-blocked-shop-store-changelog"

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


## user:client
#docker exec -i kafka-1-destination bash -c "\
#  kafka-acls --bootstrap-server kafka-1:9092 \
#  --command-config /etc/kafka/jaas/client_sasl.properties \
#  --add --allow-principal User:client \
#  --operation DESCRIBE \
#  --group client-consumer-group"
#
#docker exec -i kafka-1-destination bash -c "\
#  kafka-acls --bootstrap-server kafka-1:9092 \
#  --command-config /etc/kafka/jaas/client_sasl.properties \
#  --add --allow-principal User:client \
#  --operation READ \
#  --group client-consumer-group"
#
#docker exec -i kafka-1-destination bash -c "\
#  kafka-acls --bootstrap-server kafka-1:9092 \
#  --command-config /etc/kafka/jaas/client_sasl.properties \
#  --add --allow-principal User:client \
#  --operation READ \
#  --topic client"


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
  --operation READ \
  --topic shop"

docker exec -i kafka-1-destination bash -c "\
  kafka-acls --bootstrap-server kafka-1-destination:7092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:shop \
  --operation WRITE \
  --topic shop"

docker exec -i kafka-1-destination bash -c "\
  kafka-acls --bootstrap-server kafka-1-destination:7092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:shop \
  --operation READ \
  --group shop-consumer-group"

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
  --operation READ \
  --group shop-consumer-group-mirror"










# connect-user
docker exec -i kafka-1-reg bash -c "\
  kafka-acls --bootstrap-server kafka-1:9092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:connect-user \
  --operation ALL \
  --topic connect-offset-storage"

docker exec -i kafka-1-reg bash -c "\
  kafka-acls --bootstrap-server kafka-1:9092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:connect-user \
  --operation ALL \
  --topic connect-status-storage"

docker exec -i kafka-1-reg bash -c "\
  kafka-acls --bootstrap-server kafka-1:9092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:connect-user \
  --operation ALL \
  --topic connect-config-storage"

docker exec -i kafka-1-reg bash -c "\
  kafka-acls --bootstrap-server kafka-1:9092 \
  --command-config /etc/kafka/jaas/client_sasl.properties \
  --add --allow-principal User:connect-user \
  --operation ALL \
  --group kafka-connect"


sleep 5 && docker restart app
sleep 10 && docker restart mirror-maker


#docker exec -i kafka-1-reg kafka-topics --describe --topic shop --bootstrap-server kafka-1:9093 --command-config /etc/kafka/jaas/client_sasl.properties




#docker exec -i kafka-1-destination bash -c "kafka-topics --bootstrap-server kafka-1-destination:7092 --command-config /etc/kafka/jaas/client_sasl.properties --list"
# docker exec -i kafka-1-destination bash -c "kafka-acls --bootstrap-server kafka-1-destination:7092 --command-config /etc/kafka/jaas/client_sasl.properties --list --topic shop"


#openssl req -new -newkey rsa:2048 -keyout src/main/resources/cert/server/kafka-3/kafka-3.key -out src/main/resources/cert/server/kafka-3/kafka-3.csr -config src/main/resources/cert/server/kafka-3/kafka-3.cnf -nodes
#
#openssl x509 -req \
#    -days 3650 \
#    -in src/main/resources/cert/server/kafka-3/kafka-3.csr \
#    -CA src/main/resources/cert/root/ca.crt \
#    -CAkey src/main/resources/cert/root/ca.key \
#    -CAcreateserial \
#    -out src/main/resources/cert/server/kafka-3/kafka-3.crt \
#    -extfile src/main/resources/cert/server/kafka-3/kafka-3.cnf \
#    -extensions v3_req
#
#openssl pkcs12 -export \
#    -in src/main/resources/cert/server/kafka-3/kafka-3.crt \
#    -inkey src/main/resources/cert/server/kafka-3/kafka-3.key \
#    -chain \
#    -CAfile src/main/resources/cert/root/ca.pem \
#    -name kafka-3 \
#    -out src/main/resources/cert/server/kafka-3/kafka-3.p12 \
#    -password pass:12345678
#
#keytool -importkeystore \
#    -deststorepass 12345678 \
#    -destkeystore src/main/resources/cert/server/kafka-3/kafka.kafka-3.keystore.pkcs12 \
#    -srckeystore src/main/resources/cert/server/kafka-3/kafka-3.p12 \
#    -deststoretype PKCS12  \
#    -srcstoretype PKCS12 \
#    -noprompt \
#    -srcstorepass 12345678
#
#keytool -import \
#    -file src/main/resources/cert/root/ca.crt \
#    -alias ca \
#    -keystore src/main/resources/cert/server/kafka-3/kafka.kafka-3.truststore.jks \
#    -storepass 12345678 \
#    -noprompt
#
#echo "12345678" > src/main/resources/cert/server/kafka-3/kafka-3_sslkey_creds
#echo "12345678" > src/main/resources/cert/server/kafka-3/kafka-3_keystore_creds
#echo "12345678" > src/main/resources/cert/server/kafka-3/kafka-3_truststore_creds
#
#
#keytool -importkeystore \
#    -srckeystore src/main/resources/cert/server/kafka-3/kafka-3.p12 \
#    -srcstoretype PKCS12 \
#    -destkeystore src/main/resources/cert/server/kafka-3/kafka-3.keystore.jks \
#    -deststoretype JKS \
#    -deststorepass 12345678
#
#keytool -import -trustcacerts -file src/main/resources/cert/root/ca.crt \
#    -keystore src/main/resources/cert/server/kafka-3/kafka-3.truststore.jks \
#    -storepass 12345678 -noprompt -alias ca
#
#
#keytool -keystore client.truststore.jks -alias CARoot -import -file src/main/resources/cert/root/ca.crt -storepass 12345678 -noprompt








