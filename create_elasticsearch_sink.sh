#!/bin/bash

CONNECT_URL="http://localhost:8083/connectors"

CONNECTOR_CONFIG='{
  "name": "elasticsearch-sink",
  "config": {
    "connector.class": "io.confluent.connect.elasticsearch.ElasticsearchSinkConnector",
    "tasks.max": "1",
    "topics": "shop",
    "connection.url": "http://elasticsearch:9200",
    "type.name": "_doc",
    "key.ignore": "true",
    "schema.ignore": "false",
    "key.converter": "org.apache.kafka.connect.storage.StringConverter",
    "value.converter": "io.confluent.connect.avro.AvroConverter",
    "value.converter.schema.registry.url": "http://schema-registry:8081",
    "behavior.on.malformed.documents": "warn",
    "behavior.on.null.values": "delete",
    "batch.size": 2000,
    "errors.tolerance": "all",
    "errors.log.enable": "true"
  }
}'

# Отправка запроса
echo "Creating Elasticsearch sink connector..."
response=$(curl --location --silent --write-out "\n%{http_code}" --request POST \
  --header 'Content-Type: application/json' \
  --data "$CONNECTOR_CONFIG" \
  "$CONNECT_URL")

# Разделение response body и status code
http_code=$(echo "$response" | tail -n1)
response_body=$(echo "$response" | sed '$d')

# Проверка результата
if [ "$http_code" -eq 201 ] || [ "$http_code" -eq 409 ]; then
  echo "Connector created/updated successfully."
  echo "Response:"
  echo "$response_body" | jq .
else
  echo "Failed to create connector. HTTP Status: $http_code"
  echo "Error response:"
  echo "$response_body" | jq .
  exit 1
fi