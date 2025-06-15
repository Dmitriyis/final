#!/bin/bash

BASE_URL="http://localhost:8090/shops/blocked"

curl --location --request POST "$BASE_URL/USB-C%20Cable" \
  --header 'Content-Type: application/json' \
  --data-raw ''
echo -e "\n"


curl --location --request POST "$BASE_URL/Bluetooth%20Speaker" \
  --header 'Content-Type: application/json' \
  --data-raw ''
echo -e "\n"


curl --location --request POST "$BASE_URL/Fitness%20Tracker" \
  --header 'Content-Type: application/json' \
  --data-raw ''
echo -e "\n"