-- =========================================================
-- ProductMVC - schema day du (users + categories + products)
-- De bai: CRUD, loc category, phan trang, xoá mem
--
-- CHU Y: script nay DROP database. Neu DB da co du lieu, dung
--        sql/upgrade_assignment.sql (khong xoa data).
-- =========================================================
DROP DATABASE IF EXISTS product_mvc_db;
CREATE DATABASE product_mvc_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE product_mvc_db;

CREATE TABLE users (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    username      VARCHAR(50)  NOT NULL UNIQUE,
    password      VARCHAR(100) NOT NULL,
    full_name     VARCHAR(100) NOT NULL,
    email         VARCHAR(100),
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE categories (
    id     INT AUTO_INCREMENT PRIMARY KEY,
    name   VARCHAR(100) NOT NULL UNIQUE,
    status BOOLEAN DEFAULT TRUE
) ENGINE=InnoDB;

CREATE TABLE products (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    name          VARCHAR(255) NOT NULL,
    price         DECIMAL(12,2) NOT NULL,
    quantity      INT NOT NULL,
    description   VARCHAR(500),
    status        BOOLEAN DEFAULT TRUE,
    deleted       BOOLEAN DEFAULT FALSE,
    category_id   INT NOT NULL,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_products_category FOREIGN KEY (category_id) REFERENCES categories(id)
) ENGINE=InnoDB;

INSERT INTO users (username, password, full_name, email) VALUES
('admin', '$2a$10$NCWm1/Yq11wwqwQ3XRbPmO8VFXl0HUMHrIOdXEiMFHjD.iwNyK7Xe', 'Quan tri vien', 'admin@example.com');

INSERT INTO categories (name, status) VALUES
('Laptop', TRUE),
('Accessory', TRUE),
('Keyboard', TRUE);

INSERT INTO products (name, price, quantity, status, category_id, description) VALUES
('Laptop Dell Inspiron', 15000000, 10, TRUE, 1, 'Ultrabook 15 inch'),
('Mouse Logitech M331', 350000, 50, TRUE, 2, 'Chuot silent click'),
('Keyboard Akko 3087', 1200000, 20, TRUE, 3, 'Ban phim co 87 phim'),
('Laptop Dell XPS 13', 32990000, 10, TRUE, 1, 'RAM 16GB, SSD 512GB'),
('Man hinh LG 24MK430H', 2990000, 15, TRUE, 2, 'Man hinh 24 inch Full HD IPS'),
('Tai nghe Sony WH-1000XM4', 6990000, 8, TRUE, 2, 'Chong on chu dong');
