#!/bin/bash

# PATH 설정
export PATH=/usr/local/bin:/usr/bin:/bin:/opt/homebrew/bin

URL="http://localhost:8080/api/v1/engine"
TYPE="Content-Type: application/json"

for i in {1..100}; do
	BODY=$(cat <<EOF
{
        "carId":"$i",
        "mdn":"01234567890",
        "terminalId":"A001",
        "manufactureId":"6",
        "packetVersion":"5",
        "deviceId":"1"
}
EOF
)

curl -s -o /dev/null -w "%{http_code}\n" -X POST "$URL" \
     -H "$TYPE" \
     -d "$BODY" &
done

wait  # 모든 백그라운드 curl 요청이 끝날 때까지 대기

