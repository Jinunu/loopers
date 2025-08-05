INSERT INTO brand (id, created_at, updated_at, name, image_url) VALUES
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
(6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 6, 3);  -- 퓨마 스웨이드