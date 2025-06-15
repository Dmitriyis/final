#!/bin/bash

URL="http://localhost:8090/shops/create-candidate"
TMP_FILE=$(mktemp)

send_request() {
  local data="$1"
  echo "$data" > "$TMP_FILE"
  echo "Sending request with shop_id: $(echo "$data" | jq -r '.shop_id')"
  curl --location "$URL" \
    --header 'Content-Type: application/json' \
    --data-binary "@$TMP_FILE"
  echo -e "\n"
}

# Объект 1 (shop_id: 1)
send_request '{
  "shop_id": "1",
  "name": "Smart Watch Pro",
  "description": "Premium smart watch with health monitoring",
  "price": { "amount": 12999.99, "currency": "RUB" },
  "category": "electronics",
  "brand": "TechBrand",
  "stock": { "available": 75, "reserved": 15 },
  "sku": "SWP-001",
  "tags": ["wearable", "smart", "fitness"],
  "images": [
    { "url": "https://example.com/images/sw1.jpg", "alt": "Smart Watch front" },
    { "url": "https://example.com/images/sw2.jpg", "alt": "Smart Watch side" }
  ],
  "specifications": {
    "weight": "45g",
    "dimensions": "42x36x11mm",
    "battery": "7 days",
    "waterproof": "IP68"
  },
  "created_at": "2023-01-15T10:30:00Z",
  "updated_at": "2023-06-01T14:45:00Z",
  "index": "products",
  "store_id": "store_101"
}'

# Объект 2 (shop_id: 1)
send_request '{
  "shop_id": "1",
  "name": "Wireless Earbuds",
  "description": "Noise cancelling wireless earbuds",
  "price": { "amount": 8999.50, "currency": "RUB" },
  "category": "audio",
  "brand": "SoundMaster",
  "stock": { "available": 120, "reserved": 30 },
  "sku": "WEB-002",
  "tags": ["wireless", "audio", "bluetooth"],
  "images": [
    { "url": "https://example.com/images/eb1.jpg", "alt": "Earbuds main" },
    { "url": "https://example.com/images/eb2.jpg", "alt": "Earbuds case" }
  ],
  "specifications": {
    "weight": "12g",
    "dimensions": "24x18x22mm",
    "battery": "8 hours",
    "case_battery": "24 hours"
  },
  "created_at": "2023-02-20T09:15:00Z",
  "updated_at": "2023-05-15T11:20:00Z",
  "index": "products",
  "store_id": "store_101"
}'

# Объект 3 (shop_id: 1)
send_request '{
  "shop_id": "1",
  "name": "Bluetooth Speaker",
  "description": "Portable waterproof speaker",
  "price": { "amount": 5999.00, "currency": "RUB" },
  "category": "audio",
  "brand": "AudioPro",
  "stock": { "available": 85, "reserved": 10 },
  "sku": "BTS-003",
  "tags": ["speaker", "portable", "waterproof"],
  "images": [
    { "url": "https://example.com/images/sp1.jpg", "alt": "Speaker front" },
    { "url": "https://example.com/images/sp2.jpg", "alt": "Speaker back" }
  ],
  "specifications": {
    "weight": "800g",
    "dimensions": "18x18x8cm",
    "battery": "20 hours",
    "waterproof": "IPX7"
  },
  "created_at": "2023-03-10T08:00:00Z",
  "updated_at": "2023-06-10T16:30:00Z",
  "index": "products",
  "store_id": "store_101"
}'

# Объект 4 (shop_id: 2)
send_request '{
  "shop_id": "2",
  "name": "Fitness Tracker",
  "description": "Basic activity tracker",
  "price": { "amount": 3499.00, "currency": "RUB" },
  "category": "fitness",
  "brand": "FitLife",
  "stock": { "available": 150, "reserved": 25 },
  "sku": "FIT-004",
  "tags": ["fitness", "tracker", "health"],
  "images": [
    { "url": "https://example.com/images/ft1.jpg", "alt": "Tracker front" },
    { "url": "https://example.com/images/ft2.jpg", "alt": "Tracker band" }
  ],
  "specifications": {
    "weight": "25g",
    "dimensions": "38x10x8mm",
    "battery": "14 days",
    "waterproof": "IP67"
  },
  "created_at": "2023-04-05T11:20:00Z",
  "updated_at": "2023-07-12T09:45:00Z",
  "index": "products",
  "store_id": "store_202"
}'

# Объект 5 (shop_id: 2)
send_request '{
  "shop_id": "2",
  "name": "Power Bank 10000mAh",
  "description": "Compact power bank",
  "price": { "amount": 2499.00, "currency": "RUB" },
  "category": "accessories",
  "brand": "PowerPlus",
  "stock": { "available": 200, "reserved": 40 },
  "sku": "PBK-005",
  "tags": ["power", "charger", "portable"],
  "images": [
    { "url": "https://example.com/images/pb1.jpg", "alt": "PowerBank front" },
    { "url": "https://example.com/images/pb2.jpg", "alt": "PowerBank ports" }
  ],
  "specifications": {
    "weight": "220g",
    "dimensions": "10x6x2cm",
    "capacity": "10000mAh",
    "ports": "2 USB"
  },
  "created_at": "2023-05-12T14:10:00Z",
  "updated_at": "2023-08-01T12:30:00Z",
  "index": "products",
  "store_id": "store_202"
}'

# Объект 6 (shop_id: 2)
send_request '{
  "shop_id": "2",
  "name": "USB-C Cable",
  "description": "Durable charging cable",
  "price": { "amount": 999.00, "currency": "RUB" },
  "category": "accessories",
  "brand": "CablePro",
  "stock": { "available": 500, "reserved": 100 },
  "sku": "UCC-006",
  "tags": ["cable", "charger", "usb-c"],
  "images": [
    { "url": "https://example.com/images/uc1.jpg", "alt": "Cable full" },
    { "url": "https://example.com/images/uc2.jpg", "alt": "Cable ends" }
  ],
  "specifications": {
    "length": "1.8m",
    "current": "3A",
    "warranty": "2 years"
  },
  "created_at": "2023-06-18T13:45:00Z",
  "updated_at": "2023-09-05T10:15:00Z",
  "index": "products",
  "store_id": "store_202"
}'

# Объект 7 (shop_id: 3)
send_request '{
  "shop_id": "3",
  "name": "Wireless Mouse",
  "description": "Ergonomic wireless mouse",
  "price": { "amount": 1999.00, "currency": "RUB" },
  "category": "computers",
  "brand": "ClickMaster",
  "stock": { "available": 180, "reserved": 20 },
  "sku": "WMS-007",
  "tags": ["mouse", "wireless", "ergonomic"],
  "images": [
    { "url": "https://example.com/images/wm1.jpg", "alt": "Mouse top" },
    { "url": "https://example.com/images/wm2.jpg", "alt": "Mouse bottom" }
  ],
  "specifications": {
    "weight": "95g",
    "dimensions": "10x6x3cm",
    "battery": "12 months",
    "dpi": "1600"
  },
  "created_at": "2023-07-22T15:30:00Z",
  "updated_at": "2023-10-10T11:50:00Z",
  "index": "products",
  "store_id": "store_303"
}'

# Объект 8 (shop_id: 3)
send_request '{
  "shop_id": "3",
  "name": "Mechanical Keyboard",
  "description": "Gaming mechanical keyboard",
  "price": { "amount": 8999.00, "currency": "RUB" },
  "category": "computers",
  "brand": "KeyMaster",
  "stock": { "available": 65, "reserved": 15 },
  "sku": "MKB-008",
  "tags": ["keyboard", "mechanical", "gaming"],
  "images": [
    { "url": "https://example.com/images/kb1.jpg", "alt": "Keyboard full" },
    { "url": "https://example.com/images/kb2.jpg", "alt": "Keyboard keys" }
  ],
  "specifications": {
    "weight": "1.2kg",
    "dimensions": "44x13x3cm",
    "switches": "Red",
    "backlight": "RGB"
  },
  "created_at": "2023-08-30T12:15:00Z",
  "updated_at": "2023-11-15T14:20:00Z",
  "index": "products",
  "store_id": "store_303"
}'

# Объект 9 (shop_id: 3)
send_request '{
  "shop_id": "3",
  "name": "Laptop Stand",
  "description": "Adjustable laptop stand",
  "price": { "amount": 2999.00, "currency": "RUB" },
  "category": "accessories",
  "brand": "ErgoTech",
  "stock": { "available": 90, "reserved": 10 },
  "sku": "LST-009",
  "tags": ["stand", "laptop", "ergonomic"],
  "images": [
    { "url": "https://example.com/images/ls1.jpg", "alt": "Stand front" },
    { "url": "https://example.com/images/ls2.jpg", "alt": "Stand side" }
  ],
  "specifications": {
    "weight": "650g",
    "dimensions": "30x25x15cm",
    "material": "Aluminum",
    "adjustable": "6 levels"
  },
  "created_at": "2023-09-14T10:45:00Z",
  "updated_at": "2023-12-05T16:10:00Z",
  "index": "products",
  "store_id": "store_303"
}'

# Объект 10 (shop_id: 1)
send_request '{
  "shop_id": "1",
  "name": "Phone Case",
  "description": "Shockproof phone case",
  "price": { "amount": 1499.00, "currency": "RUB" },
  "category": "accessories",
  "brand": "CaseGuard",
  "stock": { "available": 300, "reserved": 50 },
  "sku": "PHC-010",
  "tags": ["case", "protective", "shockproof"],
  "images": [
    { "url": "https://example.com/images/pc1.jpg", "alt": "Case front" },
    { "url": "https://example.com/images/pc2.jpg", "alt": "Case back" }
  ],
  "specifications": {
    "weight": "45g",
    "material": "TPU",
    "compatibility": "iPhone 14",
    "protection": "MIL-STD-810G"
  },
  "created_at": "2023-10-25T11:30:00Z",
  "updated_at": "2024-01-10T13:15:00Z",
  "index": "products",
  "store_id": "store_101"
}'

rm "$TMP_FILE"