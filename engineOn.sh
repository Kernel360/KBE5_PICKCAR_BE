#!/bin/bash

# PATH 설정
export PATH=/usr/local/bin:/usr/bin:/bin:/opt/homebrew/bin

URL="http://localhost:8080/api/v1/engine/on"
TYPE="Content-Type: application/json"
BODY='{
        "carId":"1",
        "mdn":"01234567890",
        "terminalId":"A001",
        "manufactureId":"6",
        "packetVersion":"5",
        "deviceId":"1"
}'

curl -X POST "$URL" \
     -H "$TYPE" \
     -d "$BODY"

