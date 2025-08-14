## k6 로 Product API 부하 테스트 하기
ProductV1Controller 의 엔드포인트(`/api/v1/products`, `/api/v1/products/{id}`)를 대상으로 하는 k6 스크립트를 추가했습니다.

- 스크립트 위치: `k6/product-api.js`
- 요구 헤더: `X-USER-ID` (기본값 1로 전송)
- 기본 대상 URL: `http://localhost:8080` (환경변수로 변경 가능)

### 1) k6 CLI로 실행
PowerShell 기준 예시입니다.

- 기본 스모크 테스트
```powershell
$env:BASE_URL="http://localhost:8080"; k6 run k6/product-api.js
```

- VU/Duration 지정
```powershell
$env:BASE_URL="http://localhost:8080"; $env:VUS="50"; $env:DURATION="1m"; k6 run k6/product-api.js
```

- 단계적 램핑 (Stages)
```powershell
$env:BASE_URL="http://localhost:8080"; $env:STAGES='[{"duration":"30s","target":20},{"duration":"1m","target":100},{"duration":"30s","target":0}]'; k6 run k6/product-api.js
```

- 특정 사용자/상품 ID 지정 (선택)
```powershell
$env:USER_ID="1"; $env:PRODUCT_ID="1"; $env:PRODUCT_IDS="1,2,3,4"; k6 run k6/product-api.js
```

### 2) Docker 로 실행
Docker Desktop + PowerShell 기준 예시입니다.

- 로컬 애플리케이션 포트가 8080 이고, Docker 컨테이너에서 접근 시 `host.docker.internal` 사용
```powershell
```

Windows 경로 매핑이 동작하지 않을 경우, `${PWD}` 대신 프로젝트 절대경로를 사용하세요.

### 3) Docker Compose (infra-compose.yml)로 실행
infra-compose.yml 에 k6 서비스가 포함되어 있어, 다음과 같이 손쉽게 실행할 수 있습니다.

- 기본 실행 (기본 BASE_URL은 http://host.docker.internal:8080)
```powershell
docker-compose -f ./docker/infra-compose.yml run --rm k6
```

- 환경변수로 부하 프로파일 및 대상 URL 지정
```powershell
$env:BASE_URL="http://host.docker.internal:8080"; $env:VUS="20"; $env:DURATION="1m"; docker-compose -f ./docker/infra-compose.yml run --rm k6
```

- Stages 사용 (JSON 문자열)
```powershell
$env:STAGES='[{"duration":"30s","target":20},{"duration":"1m","target":100},{"duration":"30s","target":0}]'; docker-compose -f ./docker/infra-compose.yml run --rm k6
```

- 특정 사용자/상품 ID 지정
```powershell
$env:USER_ID="1"; $env:PRODUCT_ID="1"; $env:PRODUCT_IDS="1,2,3,4"; docker-compose -f ./docker/infra-compose.yml run --rm k6
```

참고
- Compose 의 k6 서비스는 ./k6 디렉터리를 컨테이너 /scripts 로 마운트합니다.
- k6 컨테이너는 실행 완료 후 종료됩니다(run --rm). 반복 실행 시 위 명령을 재사용하세요.

### 스크립트 동작
- 목록 API: 기본/정렬/페이지 쿼리로 랜덤 호출
- 상세 API: `PRODUCT_ID` 또는 `PRODUCT_IDS`(콤마구분)에서 랜덤 선택하여 호출
- 공통 헤더: `X-USER-ID`(기본 1)
- 기본 임계치(thresholds)
    - 실패율: `< 1%`
    - 95% 지연시간: `< 500ms`

### 결과 확인
- k6 콘솔 출력으로 요약 지표 확인
- 별도의 Prometheus/Grafana 연동을 사용 중이라면, k6 결과는 별도 exporter 가 필요합니다. 현재 레포의 Grafana/Prometheus 설정은 애플리케이션 메트릭용이며 k6 메트릭은 기본 포함되지 않습니다.
