import http from 'k6/http';
import { sleep } from 'k6';

export let options = {
  vus: 9000, // 동시 사용자 수
  duration: '2s', // 테스트 시간
};

export default function () {
  const url = 'http://localhost:8080/api/v1/engine';
  const payload = JSON.stringify({
    carId: "1",
    mdn: "01234567890",
    terminalId: "A001",
    manufactureId: "6",
    packetVersion: "5",
    deviceId: "1"
  });

  const params = {
    headers: {
      'Content-Type': 'application/json',
    },
  };

  http.post(url, payload, params);
  // sleep(1);
}

