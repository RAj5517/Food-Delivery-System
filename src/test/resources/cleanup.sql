-- Cleanup script to run after each test
-- Truncate all tables in reverse order of dependencies

TRUNCATE TABLE review CASCADE;
TRUNCATE TABLE payment CASCADE;
TRUNCATE TABLE order_item CASCADE;
TRUNCATE TABLE "order" CASCADE;
TRUNCATE TABLE cart_item CASCADE;
TRUNCATE TABLE cart CASCADE;
TRUNCATE TABLE menu_item CASCADE;
TRUNCATE TABLE category CASCADE;
TRUNCATE TABLE delivery_partner CASCADE;
TRUNCATE TABLE customer_address CASCADE;
TRUNCATE TABLE customer CASCADE;
TRUNCATE TABLE restaurant CASCADE;
TRUNCATE TABLE "user" CASCADE;

-- Reset sequences (if using PostgreSQL sequences)
-- ALTER SEQUENCE user_id_seq RESTART WITH 1;
-- ALTER SEQUENCE customer_id_seq RESTART WITH 1;
-- ... (add other sequences as needed)

