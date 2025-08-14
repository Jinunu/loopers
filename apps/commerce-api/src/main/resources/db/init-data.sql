/*INSERT INTO brand (id, created_at, updated_at, name, image_url) VALUES
(1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '나이키', 'https://example.com/brands/nike.jpg'),
(2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '아디다스', 'https://example.com/brands/adidas.jpg'),
(3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '퓨마', 'https://example.com/brands/puma.jpg');

-- 상품 데이터
INSERT INTO product (id, created_at, updated_at, name, image_url, price, quantity, brand_id) VALUES
-- 나이키 상품
(1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '나이키 에어맥스', 'https://example.com/products/nike-airmax.jpg', 129000, 50, 1),
(2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '나이키 조던', 'https://example.com/products/nike-jordan.jpg', 159000, 30, 1),
(3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '나이키 줌', 'https://example.com/products/nike-zoom.jpg', 99000, 40, 1),

-- 아디다스 상품
(4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '아디다스 슈퍼스타', 'https://example.com/products/adidas-superstar.jpg', 89000, 45, 2),
(5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '아디다스 울트라부스트', 'https://example.com/products/adidas-ultraboost.jpg', 179000, 25, 2),

-- 퓨마 상품
(6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '퓨마 스웨이드', 'https://example.com/products/puma-suede.jpg', 79000, 35, 3);

-- 사용자 데이터
INSERT INTO users (id, created_at, updated_at, user_id, email, birth_date, gender) VALUES
(1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'chulsoo', 'chulsoo.kim@email.com', '1990-01-01', 'M'),
(2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'younghee', 'younghee.lee@email.com', '1992-03-15', 'F'),
(3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'jimin', 'jimin.park@email.com', '1995-07-22', 'M');

-- 좋아요 데이터
INSERT INTO likes (id, created_at, updated_at, product_id, user_id) VALUES
-- chulsoo 좋아요
(1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 1),  -- 나이키 에어맥스
(2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 4, 1),  -- 아디다스 슈퍼스타

-- younghee 좋아요
(3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2, 2),  -- 나이키 조던
(4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5, 2),  -- 아디다스 울트라부스트

-- jimin 좋아요
(5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 3, 3),  -- 나이키 줌
(6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 6, 3);  -- 퓨마 스웨이드*/

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


-- 2. 상품 10만개 생성 (현실적인 실행 시간을 위해 필요 시 주석처리하거나 수량을 줄이세요)
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

-- 좋아요 데이터(랜덤 2만건)
INSERT IGNORE INTO likes (id, created_at, updated_at, product_id, user_id)
SELECT
    seq AS id,
    NOW() AS created_at,
    NOW() AS updated_at,
    (FLOOR(1 + (RAND() * 10000))) AS product_id,
    (FLOOR(1 + (RAND() * 3))) AS user_id
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
              (SELECT @rownum := 0) t0
         LIMIT 20000
     ) seqs;
