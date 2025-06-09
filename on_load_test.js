import http from 'k6/http';
import { sleep } from 'k6';

export let options = {
  vus: 1, // 동시 사용자 수
//  duration: '10s', // 테스트 시간
  iterations: 1
};

export default function () {
  const url = 'http://localhost:8080/api/v1/event/engine/on';
  const payload = JSON.stringify({
        car_id: 1,
        mdn: "01234567890",
        status: true,
        engine_on_time: "20240601123000",
        engine_off_time: "",
        gps_status: "NORMAL",
        latitude: 37.4418038,
        longitude: 127.244003,
        angle: 90,
        speed: 30,
        total_distance: 10000
  });

  const params = {
    headers: {
      'Content-Type': 'application/json',
    },
  };

  http.post(url, payload, params);
  // sleep(1);
}

