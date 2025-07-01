import http from 'k6/http';
import { sleep } from 'k6';

export let options = {
  vus: 1, // 동시 사용자 수
  // duration: '1m', // 테스트 시간
  iterations: 1 // 반복 횟수
};

export default function () {
    const requests = [
        {
            url: 'http://localhost:8080/api/v1/event/engine/on',
            payload: JSON.stringify({
                vehicle_id: 1,
                mdn: "01234567890",
                status: true,
                engine_on_time: "20240601123000",
                engine_off_time: "",
                gps_status: "NORMAL",
                latitude: 37.4418038,
                longitude: 12.7244003
            })
        },
        {
            url: 'http://localhost:8080/api/v1/cycle',
            payload: JSON.stringify({
                vehicle_id: 1,
                cycle_cnt: 60,
                occurred_at: "20240601123001",
                distance: 10591,
                cycle_infos: [
                    { "second": "20241130000119", "gps_status": "NORMAL", "latitude": 35.624439, "longitude": 129.335991, "angle": 0, "speed": 30, "battery": 128 },
                    { "second": "20241130000120", "gps_status": "NORMAL", "latitude": 35.624403, "longitude": 129.335968, "angle": 0, "speed": 30, "battery": 128 },
                    { "second": "20241130000121", "gps_status": "NORMAL", "latitude": 35.624352, "longitude": 129.335945, "angle": 0, "speed": 30, "battery": 128 },
                    { "second": "20241130000122", "gps_status": "NORMAL", "latitude": 35.624305, "longitude": 129.335930, "angle": 0, "speed": 30, "battery": 128 },
                    { "second": "20241130000123", "gps_status": "NORMAL", "latitude": 35.624252, "longitude": 129.335918, "angle": 0, "speed": 30, "battery": 128 },
                    { "second": "20241130000124", "gps_status": "NORMAL", "latitude": 35.624190, "longitude": 129.335906, "angle": 0, "speed": 30, "battery": 128 },
                    { "second": "20241130000125", "gps_status": "NORMAL", "latitude": 35.624126, "longitude": 129.335896, "angle": 0, "speed": 30, "battery": 128 },
                    { "second": "20241130000126", "gps_status": "NORMAL", "latitude": 35.624064, "longitude": 129.335891, "angle": 0, "speed": 30, "battery": 128 },
                    { "second": "20241130000127", "gps_status": "NORMAL", "latitude": 35.624004, "longitude": 129.335893, "angle": 0, "speed": 30, "battery": 128 },
                    { "second": "20241130000128", "gps_status": "NORMAL", "latitude": 35.623948, "longitude": 129.335896, "angle": 0, "speed": 30, "battery": 128 },
                    { "second": "20241130000129", "gps_status": "NORMAL", "latitude": 35.623891, "longitude": 129.335906, "angle": 0, "speed": 30, "battery": 128 },
                    { "second": "20241130000130", "gps_status": "NORMAL", "latitude": 35.623838, "longitude": 129.335918, "angle": 0, "speed": 30, "battery": 128 },
                    { "second": "20241130000131", "gps_status": "NORMAL", "latitude": 35.623784, "longitude": 129.335935, "angle": 0, "speed": 30, "battery": 128 },
                    { "second": "20241130000132", "gps_status": "NORMAL", "latitude": 35.623723, "longitude": 129.335950, "angle": 0, "speed": 30, "battery": 128 },
                    { "second": "20241130000133", "gps_status": "NORMAL", "latitude": 35.623652, "longitude": 129.335961, "angle": 0, "speed": 30, "battery": 128 },
                    { "second": "20241130000134", "gps_status": "NORMAL", "latitude": 35.623589, "longitude": 129.335991, "angle": 0, "speed": 30, "battery": 128 },
                    { "second": "20241130000135", "gps_status": "NORMAL", "latitude": 35.623576, "longitude": 129.336083, "angle": 0, "speed": 30, "battery": 128 },
                    { "second": "20241130000136", "gps_status": "NORMAL", "latitude": 35.623535, "longitude": 129.336153, "angle": 0, "speed": 30, "battery": 128 },
                    { "second": "20241130000137", "gps_status": "NORMAL", "latitude": 35.623489, "longitude": 129.336208, "angle": 0, "speed": 30, "battery": 128 },
                    { "second": "20241130000138", "gps_status": "NORMAL", "latitude": 35.623444, "longitude": 129.336255, "angle": 0, "speed": 30, "battery": 128 },
                    { "second": "20241130000139", "gps_status": "NORMAL", "latitude": 35.623393, "longitude": 129.336275, "angle": 0, "speed": 30, "battery": 128 },
                    { "second": "20241130000140", "gps_status": "NORMAL", "latitude": 35.623365, "longitude": 129.336296, "angle": 0, "speed": 30, "battery": 128 },
                    { "second": "20241130000141", "gps_status": "NORMAL", "latitude": 35.623349, "longitude": 129.336286, "angle": 0, "speed": 30, "battery": 128 },
                    { "second": "20241130000142", "gps_status": "NORMAL", "latitude": 35.623338, "longitude": 129.336268, "angle": 0, "speed": 30, "battery": 128 },
                    { "second": "20241130000143", "gps_status": "NORMAL", "latitude": 35.623331, "longitude": 129.336263, "angle": 0, "speed": 30, "battery": 128 },
                    { "second": "20241130000144", "gps_status": "NORMAL", "latitude": 35.623330, "longitude": 129.336263, "angle": 0, "speed": 30, "battery": 128 },
                    { "second": "20241130000145", "gps_status": "NORMAL", "latitude": 35.623332, "longitude": 129.336265, "angle": 0, "speed": 30, "battery": 128 },
                    { "second": "20241130000146", "gps_status": "NORMAL", "latitude": 35.623335, "longitude": 129.336266, "angle": 0, "speed": 30, "battery": 128 },
                    { "second": "20241130000147", "gps_status": "NORMAL", "latitude": 35.623338, "longitude": 129.336266, "angle": 0, "speed": 30, "battery": 128 },
                    { "second": "20241130000148", "gps_status": "NORMAL", "latitude": 35.623339, "longitude": 129.336266, "angle": 0, "speed": 30, "battery": 128 },
                    { "second": "20241130000149", "gps_status": "NORMAL", "latitude": 35.623340, "longitude": 129.336268, "angle": 0, "speed": 30, "battery": 128 },
                    { "second": "20241130000150", "gps_status": "NORMAL", "latitude": 35.623341, "longitude": 129.336268, "angle": 0, "speed": 30, "battery": 128 },
                    { "second": "20241130000151", "gps_status": "NORMAL", "latitude": 35.623341, "longitude": 129.336268, "angle": 0, "speed": 30, "battery": 128 },
                    { "second": "20241130000152", "gps_status": "NORMAL", "latitude": 35.623337, "longitude": 129.336265, "angle": 0, "speed": 30, "battery": 128 },
                    { "second": "20241130000153", "gps_status": "NORMAL", "latitude": 35.623333, "longitude": 129.336256, "angle": 0, "speed": 30, "battery": 128 },
                    { "second": "20241130000154", "gps_status": "NORMAL", "latitude": 35.623317, "longitude": 129.336240, "angle": 0, "speed": 30, "battery": 128 },
                    { "second": "20241130000155", "gps_status": "NORMAL", "latitude": 35.623291, "longitude": 129.336220, "angle": 0, "speed": 30, "battery": 128 },
                    { "second": "20241130000156", "gps_status": "NORMAL", "latitude": 35.623262, "longitude": 129.336203, "angle": 0, "speed": 30, "battery": 128 },
                    { "second": "20241130000157", "gps_status": "NORMAL", "latitude": 35.623227, "longitude": 129.336204, "angle": 0, "speed": 30, "battery": 128 },
                    { "second": "20241130000158", "gps_status": "NORMAL", "latitude": 35.623194, "longitude": 129.336228, "angle": 0, "speed": 30, "battery": 128 },
                    { "second": "20241130000159", "gps_status": "NORMAL", "latitude": 35.623171, "longitude": 129.336271, "angle": 0, "speed": 30, "battery": 128 },
                    { "second": "20241130000200", "gps_status": "NORMAL", "latitude": 35.623161, "longitude": 129.336333, "angle": 0, "speed": 30, "battery": 128 },
                    { "second": "20241130000201", "gps_status": "NORMAL", "latitude": 35.623151, "longitude": 129.336408, "angle": 0, "speed": 30, "battery": 128 },
                    { "second": "20241130000202", "gps_status": "NORMAL", "latitude": 35.623150, "longitude": 129.336498, "angle": 0, "speed": 30, "battery": 128 },
                    { "second": "20241130000203", "gps_status": "NORMAL", "latitude": 35.623147, "longitude": 129.336601, "angle": 0, "speed": 30, "battery": 128 },
                    { "second": "20241130000204", "gps_status": "NORMAL", "latitude": 35.623145, "longitude": 129.336716, "angle": 0, "speed": 30, "battery": 128 },
                    { "second": "20241130000205", "gps_status": "NORMAL", "latitude": 35.623142, "longitude": 129.336836, "angle": 0, "speed": 30, "battery": 128 },
                    { "second": "20241130000206", "gps_status": "NORMAL", "latitude": 35.623143, "longitude": 129.336955, "angle": 0, "speed": 30, "battery": 128 },
                    { "second": "20241130000207", "gps_status": "NORMAL", "latitude": 35.623144, "longitude": 129.337074, "angle": 0, "speed": 30, "battery": 128 },
                    { "second": "20241130000208", "gps_status": "NORMAL", "latitude": 35.623148, "longitude": 129.337205, "angle": 0, "speed": 30, "battery": 128 },
                    { "second": "20241130000209", "gps_status": "NORMAL", "latitude": 35.623156, "longitude": 129.337345, "angle": 0, "speed": 30, "battery": 128 },
                    { "second": "20241130000210", "gps_status": "NORMAL", "latitude": 35.623163, "longitude": 129.337490, "angle": 0, "speed": 30, "battery": 128 },
                    { "second": "20241130000211", "gps_status": "NORMAL", "latitude": 35.623170, "longitude": 129.337640, "angle": 0, "speed": 30, "battery": 128 },
                    { "second": "20241130000212", "gps_status": "NORMAL", "latitude": 35.623174, "longitude": 129.337793, "angle": 0, "speed": 30, "battery": 128 },
                    { "second": "20241130000213", "gps_status": "NORMAL", "latitude": 35.623195, "longitude": 129.337968, "angle": 0, "speed": 30, "battery": 128 },
                    { "second": "20241130000214", "gps_status": "NORMAL", "latitude": 35.623206, "longitude": 129.338140, "angle": 0, "speed": 30, "battery": 128 },
                    { "second": "20241130000215", "gps_status": "NORMAL", "latitude": 35.623215, "longitude": 129.338313, "angle": 0, "speed": 30, "battery": 128 },
                    { "second": "20241130000216", "gps_status": "NORMAL", "latitude": 35.623221, "longitude": 129.338486, "angle": 0, "speed": 30, "battery": 128 },
                    { "second": "20241130000217", "gps_status": "NORMAL", "latitude": 35.623225, "longitude": 129.338660, "angle": 0, "speed": 30, "battery": 128 },
                    { "second": "20241130000218", "gps_status": "NORMAL", "latitude": 35.623229, "longitude": 129.338836, "angle": 0, "speed": 30, "battery": 128 }
                ]
            })
        },
        {
            url: 'http://localhost:8080/api/v1/event/engine/off',
            payload: JSON.stringify({
                vehicle_id: 1,
                mdn: "01234567890",
                status: false,
                engine_on_time: "20240601123000",
                engine_off_time: "20240601123100",
                gps_status: "NORMAL",
                latitude: 37.4418097,
                longitude: 12.7244062
            })
        }
    ];

  const params = {
    headers: {
      'Content-Type': 'application/json',
    },
  };

  for (const req of requests) {
    http.post(req.url, req.payload, params);
  }
  // sleep(1);
}

