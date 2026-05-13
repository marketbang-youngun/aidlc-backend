# 공동구매 서비스 — Backend API

마켓뱅 공동구매 v2 MVP 백엔드

## 기술 스택

| 항목 | 버전 |
|------|------|
| Java | 17 |
| Spring Boot | 3.5.x |
| ORM | Spring Data JPA + QueryDSL 5.1.0 (jakarta) |
| DB | MySQL 8 (단일 Master) |
| 빌드 | Gradle |
| 인증 | JWT Cookie (어드민만) |
| API 문서 | Springdoc OpenAPI 2.8.x |
| 코드 생성 | Lombok |

## 실행

```bash
# MySQL 실행 (로컬)
# DB: groupbuy_db, user: root, password: root

./gradlew bootRun    # localhost:8888
```

## API 문서
- Swagger UI: http://localhost:8888/swagger-ui.html

## API 엔드포인트

### Admin API (인증 필요)
```
POST   /api/admin/v1/user/login           로그인
POST   /api/admin/v1/groupbuy             공구 등록
GET    /api/admin/v1/groupbuy/list        목록
GET    /api/admin/v1/groupbuy/{id}        상세
PUT    /api/admin/v1/groupbuy/{id}        수정
POST   /api/admin/v1/groupbuy/{id}/cancel 수동 취소
POST   /api/admin/v1/groupbuy/{id}/prepare 배송준비
POST   /api/admin/v1/groupbuy/{id}/complete 배송완료
GET    /api/admin/v1/groupbuy/{id}/participants 참여자 목록
```

### Buyer API (공개)
```
GET    /api/buyer/v1/public/groupbuy/list       진행중 목록
GET    /api/buyer/v1/public/groupbuy/{id}       상세
GET    /api/buyer/v1/public/groupbuy/{id}/status 폴링용 현황
POST   /api/buyer/v1/public/groupbuy/{id}/join   참여
POST   /api/buyer/v1/public/groupbuy/{id}/cancel 참여 취소
GET    /api/buyer/v1/public/groupbuy/my          내 참여 내역
```

### Common API
```
GET    /api/common/v1/product/fetch       상품 Mock 조회
```

## 패키지 구조
```
com.threelabs/
├── config/security/     # JWT 인증
├── controller/admin/    # 어드민 API
├── controller/buyer/    # 유저 API
├── controller/common/   # 공통 API
├── service/             # 비즈니스 로직
├── repository/          # JPA + QueryDSL
├── entity/              # 엔티티
├── dto/                 # Request/Response DTO
├── enumration/          # Enum (상태, 코드)
├── error/               # 예외 처리
├── schedule/            # @Scheduled 마감 처리
└── util/                # 유틸리티
```

## 담당 스토리
- 전체 16개 스토리의 서버 로직
- 핵심: 공구 상태 머신, 참여 동시성 제어 (비관적 락), 마감 자동 처리 (1분 스케줄러)
