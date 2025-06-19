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
                distance: 10590,
                cycle_infos: [
                    {
                        "second": "20240601123001",
                        "gps_status": "NORMAL",
                        "latitude": 37.4418038,
                        "longitude": 12.7244003,
                        "angle": 0,
                        "speed": 30,
                        "battery": 128
                    },
                    {
                        "second": "20240601123002",
                        "gps_status": "NORMAL",
                        "latitude": 37.4418039,
                        "longitude": 12.7244004,
                        "angle": 6,
                        "speed": 31,
                        "battery": 128
                    },
                    {
                        "second": "20240601123003",
                        "gps_status": "NORMAL",
                        "latitude": 37.4418040,
                        "longitude": 12.7244005,
                        "angle": 12,
                        "speed": 32,
                        "battery": 128
                    },
                    {
                        "second": "20240601123004",
                        "gps_status": "NORMAL",
                        "latitude": 37.4418041,
                        "longitude": 12.7244006,
                        "angle": 18,
                        "speed": 33,
                        "battery": 128
                    },
                    {
                        "second": "20240601123005",
                        "gps_status": "NORMAL",
                        "latitude": 37.4418042,
                        "longitude": 12.7244007,
                        "angle": 24,
                        "speed": 34,
                        "battery": 128
                    },
                    {
                        "second": "20240601123006",
                        "gps_status": "NORMAL",
                        "latitude": 37.4418043,
                        "longitude": 12.7244008,
                        "angle": 30,
                        "speed": 35,
                        "battery": 128
                    },
                    {
                        "second": "20240601123007",
                        "gps_status": "NORMAL",
                        "latitude": 37.4418044,
                        "longitude": 12.7244009,
                        "angle": 36,
                        "speed": 36,
                        "battery": 128
                    },
                    {
                        "second": "20240601123008",
                        "gps_status": "NORMAL",
                        "latitude": 37.4418045,
                        "longitude": 12.7244010,
                        "angle": 42,
                        "speed": 37,
                        "battery": 128
                    },
                    {
                        "second": "20240601123009",
                        "gps_status": "NORMAL",
                        "latitude": 37.4418046,
                        "longitude": 12.7244011,
                        "angle": 48,
                        "speed": 38,
                        "battery": 128
                    },
                    {
                        "second": "20240601123010",
                        "gps_status": "NORMAL",
                        "latitude": 37.4418047,
                        "longitude": 12.7244012,
                        "angle": 54,
                        "speed": 39,
                        "battery": 128
                    },
                    {
                        "second": "20240601123011",
                        "gps_status": "NORMAL",
                        "latitude": 37.4418048,
                        "longitude": 12.7244013,
                        "angle": 60,
                        "speed": 40,
                        "battery": 128
                    },
                    {
                        "second": "20240601123012",
                        "gps_status": "NORMAL",
                        "latitude": 37.4418049,
                        "longitude": 12.7244014,
                        "angle": 66,
                        "speed": 41,
                        "battery": 128
                    },
                    {
                        "second": "20240601123013",
                        "gps_status": "NORMAL",
                        "latitude": 37.4418050,
                        "longitude": 12.7244015,
                        "angle": 72,
                        "speed": 42,
                        "battery": 128
                    },
                    {
                        "second": "20240601123014",
                        "gps_status": "NORMAL",
                        "latitude": 37.4418051,
                        "longitude": 12.7244016,
                        "angle": 78,
                        "speed": 43,
                        "battery": 128
                    },
                    {
                        "second": "20240601123015",
                        "gps_status": "NORMAL",
                        "latitude": 37.4418052,
                        "longitude": 12.7244017,
                        "angle": 84,
                        "speed": 44,
                        "battery": 128
                    },
                    {
                        "second": "20240601123016",
                        "gps_status": "NORMAL",
                        "latitude": 37.4418053,
                        "longitude": 12.7244018,
                        "angle": 90,
                        "speed": 45,
                        "battery": 128
                    },
                    {
                        "second": "20240601123017",
                        "gps_status": "NORMAL",
                        "latitude": 37.4418054,
                        "longitude": 12.7244019,
                        "angle": 96,
                        "speed": 46,
                        "battery": 128
                    },
                    {
                        "second": "20240601123018",
                        "gps_status": "NORMAL",
                        "latitude": 37.4418055,
                        "longitude": 12.7244020,
                        "angle": 102,
                        "speed": 47,
                        "battery": 128
                    },
                    {
                        "second": "20240601123019",
                        "gps_status": "NORMAL",
                        "latitude": 37.4418056,
                        "longitude": 12.7244021,
                        "angle": 108,
                        "speed": 48,
                        "battery": 128
                    },
                    {
                        "second": "20240601123020",
                        "gps_status": "NORMAL",
                        "latitude": 37.4418057,
                        "longitude": 12.7244022,
                        "angle": 114,
                        "speed": 49,
                        "battery": 128
                    },
                    {
                        "second": "20240601123021",
                        "gps_status": "NORMAL",
                        "latitude": 37.4418058,
                        "longitude": 12.7244023,
                        "angle": 120,
                        "speed": 50,
                        "battery": 128
                    },
                    {
                        "second": "20240601123022",
                        "gps_status": "NORMAL",
                        "latitude": 37.4418059,
                        "longitude": 12.7244024,
                        "angle": 126,
                        "speed": 51,
                        "battery": 128
                    },
                    {
                        "second": "20240601123023",
                        "gps_status": "NORMAL",
                        "latitude": 37.4418060,
                        "longitude": 12.7244025,
                        "angle": 132,
                        "speed": 52,
                        "battery": 128
                    },
                    {
                        "second": "20240601123024",
                        "gps_status": "NORMAL",
                        "latitude": 37.4418061,
                        "longitude": 12.7244026,
                        "angle": 138,
                        "speed": 53,
                        "battery": 128
                    },
                    {
                        "second": "20240601123025",
                        "gps_status": "NORMAL",
                        "latitude": 37.4418062,
                        "longitude": 12.7244027,
                        "angle": 144,
                        "speed": 54,
                        "battery": 128
                    },
                    {
                        "second": "20240601123026",
                        "gps_status": "NORMAL",
                        "latitude": 37.4418063,
                        "longitude": 12.7244028,
                        "angle": 150,
                        "speed": 55,
                        "battery": 128
                    },
                    {
                        "second": "20240601123027",
                        "gps_status": "NORMAL",
                        "latitude": 37.4418064,
                        "longitude": 12.7244029,
                        "angle": 156,
                        "speed": 56,
                        "battery": 128
                    },
                    {
                        "second": "20240601123028",
                        "gps_status": "NORMAL",
                        "latitude": 37.4418065,
                        "longitude": 12.7244030,
                        "angle": 162,
                        "speed": 57,
                        "battery": 128
                    },
                    {
                        "second": "20240601123029",
                        "gps_status": "NORMAL",
                        "latitude": 37.4418066,
                        "longitude": 12.7244031,
                        "angle": 168,
                        "speed": 58,
                        "battery": 128
                    },
                    {
                        "second": "20240601123030",
                        "gps_status": "NORMAL",
                        "latitude": 37.4418067,
                        "longitude": 12.7244032,
                        "angle": 174,
                        "speed": 59,
                        "battery": 128
                    },
                    {
                        "second": "20240601123031",
                        "gps_status": "NORMAL",
                        "latitude": 37.4418068,
                        "longitude": 12.7244033,
                        "angle": 180,
                        "speed": 60,
                        "battery": 128
                    },
                    {
                        "second": "20240601123032",
                        "gps_status": "NORMAL",
                        "latitude": 37.4418069,
                        "longitude": 12.7244034,
                        "angle": 186,
                        "speed": 61,
                        "battery": 128
                    },
                    {
                        "second": "20240601123033",
                        "gps_status": "NORMAL",
                        "latitude": 37.4418070,
                        "longitude": 12.7244035,
                        "angle": 192,
                        "speed": 62,
                        "battery": 128
                    },
                    {
                        "second": "20240601123034",
                        "gps_status": "NORMAL",
                        "latitude": 37.4418071,
                        "longitude": 12.7244036,
                        "angle": 198,
                        "speed": 63,
                        "battery": 128
                    },
                    {
                        "second": "20240601123035",
                        "gps_status": "NORMAL",
                        "latitude": 37.4418072,
                        "longitude": 12.7244037,
                        "angle": 204,
                        "speed": 64,
                        "battery": 128
                    },
                    {
                        "second": "20240601123036",
                        "gps_status": "NORMAL",
                        "latitude": 37.4418073,
                        "longitude": 12.7244038,
                        "angle": 210,
                        "speed": 65,
                        "battery": 128
                    },
                    {
                        "second": "20240601123037",
                        "gps_status": "NORMAL",
                        "latitude": 37.4418074,
                        "longitude": 12.7244039,
                        "angle": 216,
                        "speed": 66,
                        "battery": 128
                    },
                    {
                        "second": "20240601123038",
                        "gps_status": "NORMAL",
                        "latitude": 37.4418075,
                        "longitude": 12.7244040,
                        "angle": 222,
                        "speed": 67,
                        "battery": 128
                    },
                    {
                        "second": "20240601123039",
                        "gps_status": "NORMAL",
                        "latitude": 37.4418076,
                        "longitude": 12.7244041,
                        "angle": 228,
                        "speed": 68,
                        "battery": 128
                    },
                    {
                        "second": "20240601123040",
                        "gps_status": "NORMAL",
                        "latitude": 37.4418077,
                        "longitude": 12.7244042,
                        "angle": 234,
                        "speed": 69,
                        "battery": 128
                    },
                    {
                        "second": "20240601123041",
                        "gps_status": "NORMAL",
                        "latitude": 37.4418078,
                        "longitude": 12.7244043,
                        "angle": 240,
                        "speed": 70,
                        "battery": 128
                    },
                    {
                        "second": "20240601123042",
                        "gps_status": "NORMAL",
                        "latitude": 37.4418079,
                        "longitude": 12.7244044,
                        "angle": 246,
                        "speed": 71,
                        "battery": 128
                    },
                    {
                        "second": "20240601123043",
                        "gps_status": "NORMAL",
                        "latitude": 37.4418080,
                        "longitude": 12.7244045,
                        "angle": 252,
                        "speed": 72,
                        "battery": 128
                    },
                    {
                        "second": "20240601123044",
                        "gps_status": "NORMAL",
                        "latitude": 37.4418081,
                        "longitude": 12.7244046,
                        "angle": 258,
                        "speed": 73,
                        "battery": 128
                    },
                    {
                        "second": "20240601123045",
                        "gps_status": "NORMAL",
                        "latitude": 37.4418082,
                        "longitude": 12.7244047,
                        "angle": 264,
                        "speed": 74,
                        "battery": 128
                    },
                    {
                        "second": "20240601123046",
                        "gps_status": "NORMAL",
                        "latitude": 37.4418083,
                        "longitude": 12.7244048,
                        "angle": 270,
                        "speed": 75,
                        "battery": 128
                    },
                    {
                        "second": "20240601123047",
                        "gps_status": "NORMAL",
                        "latitude": 37.4418084,
                        "longitude": 12.7244049,
                        "angle": 276,
                        "speed": 76,
                        "battery": 128
                    },
                    {
                        "second": "20240601123048",
                        "gps_status": "NORMAL",
                        "latitude": 37.4418085,
                        "longitude": 12.7244050,
                        "angle": 282,
                        "speed": 77,
                        "battery": 128
                    },
                    {
                        "second": "20240601123049",
                        "gps_status": "NORMAL",
                        "latitude": 37.4418086,
                        "longitude": 12.7244051,
                        "angle": 288,
                        "speed": 78,
                        "battery": 128
                    },
                    {
                        "second": "20240601123050",
                        "gps_status": "NORMAL",
                        "latitude": 37.4418087,
                        "longitude": 12.7244052,
                        "angle": 294,
                        "speed": 79,
                        "battery": 128
                    },
                    {
                        "second": "20240601123051",
                        "gps_status": "NORMAL",
                        "latitude": 37.4418088,
                        "longitude": 12.7244053,
                        "angle": 300,
                        "speed": 0,
                        "battery": 128
                    },
                    {
                        "second": "20240601123052",
                        "gps_status": "NORMAL",
                        "latitude": 37.4418089,
                        "longitude": 12.7244054,
                        "angle": 306,
                        "speed": 1,
                        "battery": 128
                    },
                    {
                        "second": "20240601123053",
                        "gps_status": "NORMAL",
                        "latitude": 37.4418090,
                        "longitude": 12.7244055,
                        "angle": 312,
                        "speed": 2,
                        "battery": 128
                    },
                    {
                        "second": "20240601123054",
                        "gps_status": "NORMAL",
                        "latitude": 37.4418091,
                        "longitude": 12.7244056,
                        "angle": 318,
                        "speed": 3,
                        "battery": 128
                    },
                    {
                        "second": "20240601123055",
                        "gps_status": "NORMAL",
                        "latitude": 37.4418092,
                        "longitude": 12.7244057,
                        "angle": 324,
                        "speed": 4,
                        "battery": 128
                    },
                    {
                        "second": "20240601123056",
                        "gps_status": "NORMAL",
                        "latitude": 37.4418093,
                        "longitude": 12.7244058,
                        "angle": 330,
                        "speed": 5,
                        "battery": 128
                    },
                    {
                        "second": "20240601123057",
                        "gps_status": "NORMAL",
                        "latitude": 37.4418094,
                        "longitude": 12.7244059,
                        "angle": 336,
                        "speed": 6,
                        "battery": 128
                    },
                    {
                        "second": "20240601123058",
                        "gps_status": "NORMAL",
                        "latitude": 37.4418095,
                        "longitude": 12.7244060,
                        "angle": 342,
                        "speed": 7,
                        "battery": 128
                    },
                    {
                        "second": "20240601123059",
                        "gps_status": "NORMAL",
                        "latitude": 37.4418096,
                        "longitude": 12.7244061,
                        "angle": 348,
                        "speed": 8,
                        "battery": 128
                    },
                    {
                        "second": "20240601123100",
                        "gps_status": "NORMAL",
                        "latitude": 37.4418097,
                        "longitude": 12.7244062,
                        "angle": 354,
                        "speed": 9,
                        "battery": 128
                    }
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

