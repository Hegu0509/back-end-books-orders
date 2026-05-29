
USE ordersdb;

SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE order_items;
TRUNCATE TABLE orders;
SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO orders (id, user_id, status, total_amount, currency, created_at) VALUES
(1, 'user-demo', 'CONFIRMED', 57.11, 'EUR', '2024-05-01 10:15:00');

INSERT INTO order_items (order_id, book_id, book_title, isbn, quantity, unit_price, subtotal) VALUES
(1, 1, 'El Invisible Silencio 1', '9780000000001', 1, 8.09, 8.09),
(1, 5, 'Sistemas que Aprenden 5', '9780000000005', 2, 24.51, 49.02);

INSERT INTO orders (id, user_id, status, total_amount, currency, created_at) VALUES
(2, 'user-demo', 'CONFIRMED', 29.33, 'EUR', '2024-05-03 18:42:00');

INSERT INTO order_items (order_id, book_id, book_title, isbn, quantity, unit_price, subtotal) VALUES
(2, 7, 'Historias de Papel 7', '9780000000007', 1, 29.33, 29.33);

SELECT COUNT(*) AS total_orders FROM orders;
SELECT COUNT(*) AS total_order_items FROM order_items;
