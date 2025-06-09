import http from 'k6/http';
import { sleep } from 'k6';

export let options = {
  vus: 1, // 동시 사용자 수
//  duration: '10s', // 테스트 시간
  iterations: 1
};

export default function () {
  const url = 'http://localhost:8080/api/v1/cycle';
  const payload = JSON.stringify({
      car_id: 1,
      cycle_cnt: 1,
      occurred_at: "20240601123000",
      cycle_infos: {
          "0": {
              second: "20240601123001",
              gps_status: "NORMAL",
              latitude: 374418038,
              longitude: 127244003,
              angle: 90,
              speed: 30,
              total_distance: 10000,
              battery: 128
          }
      }
  });

  const params = {
    headers: {
      'Content-Type': 'application/json',
    },
  };

  http.post(url, payload, params);
  // sleep(1);
}

