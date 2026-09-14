-- Bo sung SKU, mo ta, ProductDetail cho data dang co (khong DROP).
USE product_mvc_db;

UPDATE categories SET description = 'May tinh xach tay, ultrabook' WHERE id = 1 AND (description IS NULL OR description = '');
UPDATE categories SET description = 'Chuot, ban phim, tai nghe' WHERE id = 2 AND (description IS NULL OR description = '');
UPDATE categories SET description = 'Man hinh may tinh' WHERE id = 3 AND (description IS NULL OR description = '');

UPDATE products SET sku = CONCAT('PRD-', LPAD(id, 4, '0')) WHERE sku IS NULL OR sku = '';

UPDATE products SET description = 'Laptop Dell van phong 15 inch, phu hop hoc tap va lam viec.' WHERE id = 1;
UPDATE products SET description = 'Chuot khong day Logitech, click em, pin lau.' WHERE id = 2;
UPDATE products SET description = 'Ban phim co Akko 87 phim, switch em, layout nho gon.' WHERE id = 3;
UPDATE products SET description = 'Chuot co day Logitech, nhe, phu hop van phong.' WHERE id = 4;

INSERT INTO product_details (id, manufacturer, warranty_months, origin, description, technical_spec)
SELECT p.id, NULL, 0, NULL, NULL, NULL
FROM products p
WHERE p.id NOT IN (SELECT d.id FROM product_details d);

UPDATE product_details SET
    manufacturer = 'Dell',
    warranty_months = 12,
    origin = 'China',
    description = 'Dong Inspiron huong toi hoc sinh, sinh vien. Vo nhua ben, ban phim full-size.',
    technical_spec = 'Man 15.6 inch Full HD, RAM 8GB, SSD 256GB, Intel Core i5, Windows 11.'
WHERE id = 1;

UPDATE product_details SET
    manufacturer = 'Logitech',
    warranty_months = 24,
    origin = 'China',
    description = 'Chuot silent click, keo nhe, phu hop van phong khong muon gay on.',
    technical_spec = 'Ket noi USB receiver 2.4GHz, cam bien 1000 DPI, pin AA, thoi luong ~18 thang.'
WHERE id = 2;

UPDATE product_details SET
    manufacturer = 'Akko',
    warranty_months = 12,
    origin = 'China',
    description = 'Ban phim co 87 phim, layout TKL, keycap PBT, de mang di lam / hoc.',
    technical_spec = 'Switch Akko CS, ket noi USB-C, keycap PBT dye-sub, NKRO.'
WHERE id = 3;

UPDATE product_details SET
    manufacturer = 'Logitech',
    warranty_months = 12,
    origin = 'China',
    description = 'Chuot co day gia re, nhe, phu hop may tinh de ban.',
    technical_spec = 'USB co day, DPI 1000, 3 nut, chieu dai cap 1.8m.'
WHERE id = 4;

INSERT INTO products (sku, name, price, quantity, description, status, deleted, category_id)
SELECT 'PRD-0005', 'Laptop Dell XPS 13', 32990000, 10, 'Ultrabook mong nhe, man hinh sac net, RAM 16GB.', TRUE, FALSE, 1
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM products WHERE sku = 'PRD-0005');

INSERT INTO products (sku, name, price, quantity, description, status, deleted, category_id)
SELECT 'PRD-0006', 'Man hinh LG 24MK430H', 2990000, 15, 'Man hinh 24 inch IPS Full HD, goc nhin rong.', TRUE, FALSE, 3
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM products WHERE sku = 'PRD-0006');

INSERT INTO products (sku, name, price, quantity, description, status, deleted, category_id)
SELECT 'PRD-0007', 'Tai nghe Sony WH-1000XM4', 6990000, 8, 'Chong on chu dong, pin lau, de mang di.', TRUE, FALSE, 2
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM products WHERE sku = 'PRD-0007');

INSERT INTO product_details (id, manufacturer, warranty_months, origin, description, technical_spec)
SELECT p.id, 'Dell', 24, 'China',
       'XPS 13 vo kim loai, ban le chat, phu hop di chuyen nhieu.',
       'Man 13.4 inch FHD+, RAM 16GB, SSD 512GB, Intel Core i7.'
FROM products p
WHERE p.sku = 'PRD-0005'
  AND p.id NOT IN (SELECT d.id FROM product_details d);

INSERT INTO product_details (id, manufacturer, warranty_months, origin, description, technical_spec)
SELECT p.id, 'LG', 24, 'Korea',
       'Tam IPS, mau on dinh, phu hop lam viec van phong va xem phim.',
       '24 inch Full HD IPS, 75Hz, HDMI, analog, loa 2x5W.'
FROM products p
WHERE p.sku = 'PRD-0006'
  AND p.id NOT IN (SELECT d.id FROM product_details d);

INSERT INTO product_details (id, manufacturer, warranty_months, origin, description, technical_spec)
SELECT p.id, 'Sony', 12, 'Malaysia',
       'Chong on chu dong tot, app dieu chinh EQ, pin den 30 gio.',
       'Bluetooth 5.0, ANC, sac USB-C, driver 40mm.'
FROM products p
WHERE p.sku = 'PRD-0007'
  AND p.id NOT IN (SELECT d.id FROM product_details d);
