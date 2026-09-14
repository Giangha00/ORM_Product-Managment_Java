-- Nang cap product_mvc_db cho ORM (giu data).
-- Hibernate hbm2ddl.auto=update cung tu them cot; script nay de nop bai / chay tay.
USE product_mvc_db;

ALTER TABLE categories
    ADD COLUMN description VARCHAR(255) NULL;

ALTER TABLE products
    ADD COLUMN sku VARCHAR(50) NULL UNIQUE;

CREATE TABLE IF NOT EXISTS product_details (
    id              INT PRIMARY KEY,
    manufacturer    VARCHAR(100),
    warranty_months INT DEFAULT 0,
    origin          VARCHAR(100),
    description     VARCHAR(500),
    technical_spec  TEXT,
    CONSTRAINT fk_detail_product FOREIGN KEY (id) REFERENCES products(id)
);
