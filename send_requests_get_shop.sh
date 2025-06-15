#!/bin/bash

BASE_URL="http://localhost:8090/shops"

# Функция для URL-кодирования (без jq)
urlencode() {
    local string="${1}"
    local strlen=${#string}
    local encoded=""
    local pos c o

    for (( pos=0 ; pos<strlen ; pos++ )); do
        c=${string:$pos:1}
        case "$c" in
            [-_.~a-zA-Z0-9] ) o="${c}" ;;
            * )               printf -v o '%%%02x' "'$c"
        esac
        encoded+="${o}"
    done
    echo "${encoded}"
}

send_request() {
    local shop_name="$1"
    local client_id="$2"

    # Используем нашу функцию urlencode
    local encoded_shop_name=$(urlencode "$shop_name")

    local full_url="${BASE_URL}/${encoded_shop_name}/${client_id}"

    echo "Sending request: $full_url"
    curl --location --show-error --fail "$full_url"
    echo -e "\n----------------------------------------"
}

# Запросы для clientId=1
send_request "Wireless Earbuds" "1"
send_request "Wireless Earbuds" "1"
send_request "Wireless Earbuds" "1"
send_request "Wireless Earbuds" "1"
send_request "Wireless Earbuds" "1"
send_request "Wireless Earbuds" "1"
send_request "Wireless Earbuds" "1"
send_request "Wireless Earbuds" "1"
send_request "Wireless Earbuds" "1"
send_request "Power Bank 10000mAh" "1"
send_request "Power Bank 10000mAh" "1"
send_request "Power Bank 10000mAh" "1"
send_request "Power Bank 10000mAh" "1"

sleep 1

# Запросы для clientId=2
send_request "Wireless Earbuds" "2"
send_request "Wireless Earbuds" "2"
send_request "Smart Watch Pro" "2"
send_request "Smart Watch Pro" "2"
send_request "Mechanical Keyboard" "2"
send_request "Mechanical Keyboard" "2"
send_request "Mechanical Keyboard" "2"
send_request "Mechanical Keyboard" "2"
send_request "Wireless Mouse" "2"
send_request "Wireless Mouse" "2"

sleep 1

# Запросы для clientId=3
send_request "Power Bank 10000mAh" "3"
send_request "Power Bank 10000mAh" "3"
send_request "Power Bank 10000mAh" "3"
send_request "Phone Case" "3"
send_request "Phone Case" "3"
send_request "Phone Case" "3"
send_request "Mechanical Keyboard" "3"
send_request "Mechanical Keyboard" "3"
send_request "Mechanical Keyboard" "3"
send_request "Wireless Mouse" "3"
send_request "Wireless Mouse" "3"
send_request "Wireless Mouse" "3"
send_request "Wireless Earbuds" "3"
send_request "Wireless Earbuds" "3"
send_request "Wireless Earbuds" "3"