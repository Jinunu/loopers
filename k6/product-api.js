import http from 'k6/http';
import { check, sleep } from 'k6';
import { Trend, Counter } from 'k6/metrics';

/*****************************************************
 Product API k6 test
 Targets ProductV1Controller endpoints:
  - GET /api/v1/products
  - GET /api/v1/products/{id}

 Usage examples:
 1) CLI
   BASE_URL=http://localhost:8080 k6 run k6/product-api.js
   BASE_URL=http://localhost:8080 VUS=50 DURATION=1m k6 run k6/product-api.js
   BASE_URL=http://localhost:8080 STAGES='[{"duration":"30s","target":20},{"duration":"1m","target":100},{"duration":"30s","target":0}]' k6 run k6/product-api.js

 2) Docker (PowerShell)
   docker run --rm -it -v "${PWD}/k6:/scripts" -e BASE_URL=http://host.docker.internal:8080 grafana/k6 run /scripts/product-api.js
*****************************************************/

// Environment variables with sensible defaults
const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';
const USER_ID = __ENV.USER_ID || '1'; // sent as X-USER-ID header
const PRODUCT_ID = __ENV.PRODUCT_ID || '1'; // for detail API

// Load profile
const VUS = Number(__ENV.VUS || 10);
const DURATION = __ENV.DURATION || '30s';

// Optional staged profile via JSON string (takes precedence over VUS/DURATION when provided)
let parsedStages = null;
if (__ENV.STAGES) {
  try {
    parsedStages = JSON.parse(__ENV.STAGES);
  } catch (e) {
    // ignore parse error; will fall back to VUS/DURATION
  }
}

export const options = parsedStages
  ? {
      thresholds: {
        http_req_failed: ['rate<0.01'], // <1% errors
        http_req_duration: ['p(95)<500'], // 95% requests under 500ms
      },
      scenarios: {
        staged: {
          executor: 'ramping-vus',
          stages: parsedStages,
        },
      },
    }
  : {
      vus: VUS,
      duration: DURATION,
      thresholds: {
        http_req_failed: ['rate<0.01'],
        http_req_duration: ['p(95)<500'],
      },
    };

// Custom metrics
const listLatency = new Trend('product_list_latency', true);
const detailLatency = new Trend('product_detail_latency', true);
const listErrors = new Counter('product_list_errors');
const detailErrors = new Counter('product_detail_errors');

const headers = {
  'X-USER-ID': USER_ID,
};

// Common query combinations for list API
const listQueries = [
  '',
  '?page=0&size=10',
  '?sortField=price&sortDirection=asc&page=0&size=10',
  '?sortField=price&sortDirection=desc&page=0&size=10',
  '?sortField=createdAt&sortDirection=desc&page=0&size=20',
  '?sortField=likeCount&sortDirection=desc&page=0&size=20',
  '?brandId=1&sortField=likeCount&sortDirection=desc&page=0&size=20',
];

export default function () {
  // 1) List API
  const listQuery = listQueries[Math.floor(Math.random() * listQueries.length)];
  const listRes = http.get(`${BASE_URL}/api/v1/products${listQuery}`, { headers });
  listLatency.add(listRes.timings.duration);
  const listOk = check(listRes, {
    'list status is 2xx': (r) => r.status >= 200 && r.status < 300,
    'list has ApiResponse wrapper': (r) => typeof r.json()?.data !== 'undefined',
  });
  if (!listOk) listErrors.add(1);

  // 2) Detail API
  const id = __ENV.PRODUCT_IDS
    ? randomPick(__ENV.PRODUCT_IDS)
    : PRODUCT_ID; // allow comma-separated product IDs via env
  const detailRes = http.get(`${BASE_URL}/api/v1/products/${id}`, { headers });
  detailLatency.add(detailRes.timings.duration);
  const detailOk = check(detailRes, {
    'detail status is 2xx': (r) => r.status >= 200 && r.status < 300,
    'detail has productId': (r) => (r.json()?.data?.productId ?? null) !== null,
  });
  if (!detailOk) detailErrors.add(1);

  sleep(1);
}

function randomPick(csv) {
  const arr = String(csv)
    .split(',')
    .map((s) => s.trim())
    .filter((s) => s.length > 0);
  if (arr.length === 0) return PRODUCT_ID;
  return arr[Math.floor(Math.random() * arr.length)];
}
