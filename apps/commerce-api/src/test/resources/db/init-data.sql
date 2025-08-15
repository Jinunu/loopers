
-- 1. 브랜드 100개 생성
INSERT INTO brand (id, name, image_url, created_at, updated_at)
SELECT
    seq AS id,
    CONCAT('브랜드', seq) AS name,
    CONCAT('https://example.com/brand/', seq, '.jpg') AS image_url,
    NOW() AS created_at,
    NOW() AS updated_at
FROM (
         SELECT @rownum := @rownum + 1 AS seq
         FROM (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
               UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) t1,
              (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
               UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) t2,
              (SELECT @rownum := 0) t0
         LIMIT 100
     ) seqs;


-- 상품 생성 시간, 가격 인덱스 생성
CREATE INDEX idx_product_created_at ON product (created_at);
CREATE INDEX idx_product_price ON product (price);
CREATE INDEX idx_product_like_count ON product (like_count);

INSERT INTO product (id, name, image_url, price, quantity, brand_id, created_at, updated_at)
SELECT
    seq AS id,
    CONCAT(
            ELT((seq % 10) + 1, '나이키','아디다스','푸마','아식스','뉴발란스','언더아머','컨버스','리복','스케쳐스','반스'),
            ' ',
            ELT((seq % 6) + 1, '운동화','런닝화','농구화','축구화','슬리퍼','샌들'),
            ' ',
            seq
    ) AS name,
    CONCAT('https://example.com/product/', seq, '.jpg') AS image_url,
    ROUND((RAND() * (500000 - 10000) + 10000), 2) AS price,
    FLOOR(RAND() * 500) AS quantity,
    (seq % 10) + 1 AS brand_id,
    NOW() - INTERVAL (seq % 365) DAY AS created_at,
    NOW() - INTERVAL (seq % 365) DAY AS updated_at
FROM (
         SELECT @rownum := @rownum + 1 AS seq
         FROM (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
               UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) t1,
              (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
               UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) t2,
              (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
               UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) t3,
              (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
               UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) t4,
              (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
               UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) t5,
              (SELECT @rownum := 0) t0
         LIMIT 100000
     ) seqs;




-- 사용자 데이터
INSERT INTO users (id, created_at, updated_at, user_id, email, birth_date, gender) VALUES
                                                                                       (1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'chulsoo', 'chulsoo.kim@email.com', '1990-01-01', 'M'),
                                                                                       (2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'younghee', 'younghee.lee@email.com', '1992-03-15', 'F'),
                                                                                       (3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'jimin', 'jimin.park@email.com', '1995-07-22', 'M');

-- 추가 사용자 997명 생성 (ID 4 ~ 1003)
INSERT INTO users (id, created_at, updated_at, user_id, email, birth_date, gender)
SELECT
    3 + seq AS id,
    NOW() AS created_at,
    NOW() AS updated_at,
    CONCAT('user', LPAD(3 + seq, 4, '0')) AS user_id,
    CONCAT('user', LPAD(3 + seq, 4, '0'), '@email.com') AS email,
    DATE_FORMAT(DATE_ADD('1970-01-01', INTERVAL FLOOR(RAND() * 13150) DAY), '%Y-%m-%d') AS birth_date,
    IF(seq % 2 = 0, 'M', 'F') AS gender
FROM (
         SELECT @rownum_u := @rownum_u + 1 AS seq
         FROM (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
               UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) u1,
              (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
               UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) u2,
              (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
               UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) u3,
              (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
               UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) u4,
              (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
               UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) u5,
              (SELECT @rownum_u := 0) init
         LIMIT 997
     ) seqs;

-- 좋아요 데이터 (샘플 고정 데이터)
INSERT INTO likes (id, created_at, updated_at, product_id, user_id) VALUES
-- chulsoo 좋아요
(1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 1),
(2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 4, 1),
-- younghee 좋아요
(3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2, 2),
(4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5, 2),
-- jimin 좋아요
(5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 3, 3),
(6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 6, 3);

-- 좋아요 데이터 (다양한 분포)
-- 1) 인기 상품 구간: 상품 1~1000에 많은 좋아요 분포 (약 200,000건 시도)
INSERT IGNORE INTO likes (id, created_at, updated_at, product_id, user_id)
SELECT
    100000000 + seq AS id,
    NOW() AS created_at,
    NOW() AS updated_at,
    1 + FLOOR(RAND() * 1000) AS product_id,
    1 + FLOOR(RAND() * 1003) AS user_id
FROM (
         SELECT @rownum_l1 := @rownum_l1 + 1 AS seq
         FROM (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
               UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) l1,
              (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
               UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) l2,
              (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
               UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) l3,
              (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
               UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) l4,
              (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
               UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) l5,
              (SELECT @rownum_l1 := 0) init
         LIMIT 200000
     ) seqs;

-- 2) 중간 인기 구간: 상품 1001~10000에 중간 정도의 좋아요 (약 150,000건 시도)
INSERT IGNORE INTO likes (id, created_at, updated_at, product_id, user_id)
SELECT
    200000000 + seq AS id,
    NOW() AS created_at,
    NOW() AS updated_at,
    1001 + FLOOR(RAND() * 9000) AS product_id,
    1 + FLOOR(RAND() * 1003) AS user_id
FROM (
         SELECT @rownum_l2 := @rownum_l2 + 1 AS seq
         FROM (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
               UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) l1,
              (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
               UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) l2,
              (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
               UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) l3,
              (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
               UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) l4,
              (SELECT @rownum_l2 := 0) init
         LIMIT 150000
     ) seqs;

-- 3) 롱테일 구간: 상품 10001~100000에 소수의 좋아요 (약 50,000건 시도)
INSERT IGNORE INTO likes (id, created_at, updated_at, product_id, user_id)
SELECT
    300000000 + seq AS id,
    NOW() AS created_at,
    NOW() AS updated_at,
    10001 + FLOOR(RAND() * 90000) AS product_id,
    1 + FLOOR(RAND() * 1003) AS user_id
FROM (
         SELECT @rownum_l3 := @rownum_l3 + 1 AS seq
         FROM (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
               UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) l1,
              (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
               UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) l2,
              (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
               UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) l3,
              (SELECT @rownum_l3 := 0) init
         LIMIT 50000
     ) seqs;

-- 비정규화 동기화: Product.like_count 업데이트
UPDATE product p
LEFT JOIN (
    SELECT product_id, COUNT(*) AS cnt
    FROM likes
    GROUP BY product_id
) l ON l.product_id = p.id
SET p.like_count = COALESCE(l.cnt, 0)
WHERE IFNULL(p.like_count, -1) <> COALESCE(l.cnt, 0);