# ProductMVC — JSP + Servlet + JPA/Hibernate

Quan ly san pham theo MVC: **Controller → Service → Repository → Entity**.
CRUD Product + ProductDetail trong mot transaction. Login van dung JDBC (`UserRepository`).

## Chay

1. MySQL: database `product_mvc_db` (giu DB cu). Lan dau Hibernate `update` se them cot `sku`, `categories.description`, bang `product_details` neu chua co.
2. Hoac chay `sql/upgrade_orm.sql` roi de `hbm2ddl.auto=validate`.
3. IntelliJ: **Tomcat 11**, mo `http://localhost:8080/ProductMVC/`
4. Tai khoan: **admin / admin123**

`src/main/resources/db.properties` dung cho login JDBC; `META-INF/persistence.xml` dung cho Hibernate (cung `product_mvc_db`).

## URL

| URL | Method | Chuc nang |
| --- | --- | --- |
| `/products` | GET | List, keyword/SKU, category, status, gia, sort, phan trang |
| `/products/create` | GET/POST | Form Product + Detail / luu 1 transaction |
| `/products/edit?id=` | GET/POST | Sua Product + Detail |
| `/products/delete?id=` | GET/POST | Xoa mem (`deleted = true`) |
| `/categories` | GET/POST | Them/sua danh muc; khong ngung duoc neu con SP dang ban |

## Cau truc

```
controller/    ProductServlet, CategoryServlet, LoginServlet
service/       ProductService, CategoryService
repository/    ProductRepository, CategoryRepository, UserRepository
entity/        Product, Category, ProductDetail, User
view/          webapp/WEB-INF/views (JSP)
dto/           ProductFormDTO, ProductSearchDTO
util/          JPAUtil, DBConnection
```
