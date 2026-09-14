-- =========================================================
-- Nang cap DB hien tai product_mvc_db (GIU NGUYEN du lieu)
-- Chay trong phpMyAdmin: chon database product_mvc_db -> tab SQL
-- Neu cot da ton tai, dong ALTER tuong ung se bao Duplicate column -> bo qua dong do.
-- =========================================================
USE product_mvc_db;

-- Danh muc: de tai can cot status (chi chon danh muc dang hoat dong)
ALTER TABLE categories
    ADD COLUMN status BOOLEAN DEFAULT TRUE;

UPDATE categories SET status = TRUE WHERE status IS NULL;

-- San pham: xoá mem + khoa ngoai toi categories
ALTER TABLE products
    ADD COLUMN deleted BOOLEAN DEFAULT FALSE;

ALTER TABLE products
    ADD COLUMN category_id INT NULL;

ALTER TABLE products
    ADD COLUMN status BOOLEAN DEFAULT TRUE;

UPDATE products SET deleted = FALSE WHERE deleted IS NULL;
UPDATE products SET status = TRUE WHERE status IS NULL;

-- Gan category mac dinh cho san pham chua co danh muc (lay id nho nhat)
UPDATE products
SET category_id = (SELECT id FROM categories ORDER BY id LIMIT 1)
WHERE category_id IS NULL;

-- Khoa ngoai (neu da ton tai thi bo qua loi)
ALTER TABLE products
    ADD CONSTRAINT fk_products_category
        FOREIGN KEY (category_id) REFERENCES categories(id);
