# Bài Tập 07 & GraphQL API: RESTful API, GraphQL & Thymeleaf AJAX CRUD

**Sinh viên thực hiện:** Trần Thanh Luôn  
**Mã số sinh viên:** 24110280  
**Repository GitHub:** [https://github.com/TLuon/BTAP07_WEB_MUC4](https://github.com/TLuon/BTAP07_WEB_MUC4)

---

## 📖 Giới Thiệu Dự Án

Dự án được xây dựng trên nền tảng **Spring Boot, GraphQL API, RESTful API và Giao diện Thymeleaf / JSP AJAX**, hoàn thiện toàn bộ các tính năng tiên tiến về **Truy vấn GraphQL, Phân trang, Tìm kiếm live và Thao tác CRUD không tải lại trang** trên 2 đối tượng dữ liệu chính: `Category` (Danh mục) và `Product` (Sản phẩm).

---

## 🛠 Công Nghệ Sử Dụng

- **Backend Architecture:**
  - Java 21, Spring Boot 4.0.8, Spring MVC, Spring Data JPA, Spring Security.
  - **Spring GraphQL (`spring-boot-starter-graphql`):** Định nghĩa GraphQL Schema (`schema.graphqls`), các annotation `@Controller`, `@QueryMapping`, `@MutationMapping`, `@SchemaMapping`, `@Argument`.
  - **RESTful API:** Controllers API tại `vn.iotstar.controllers.api`, Data Wrapper `Response(Boolean status, String message, Object body)`.
  - **Storage Service:** `IStorageService` (`StorageServiceImpl`) xử lý upload và lưu trữ tập tin hình ảnh/icon.
- **Database:** Microsoft SQL Server (`jakartaJPA` database).
- **Frontend AJAX & UI Design System:**
  - **Thymeleaf Giao diện Hiện đại:** Bootstrap 5.3.2, FontAwesome 6, SweetAlert2, Google Fonts (`Plus Jakarta Sans`), CSS Glassmorphism & Micro-animations.
  - **JS AJAX Fetch GraphQL:** Hàm `fetchGraphQL(query, variables)` tương tác với `/graphql` endpoint không reload trang.
  - **JSP / jQuery REST AJAX:** Tích hợp giao diện JSP cũ sử dụng jQuery AJAX.

---

## 🚀 Tính Năng GraphQL API (`/graphql` & `/graphiql`)

### 1. Schema GraphQL (`src/main/resources/graphql/schema.graphqls`)

```graphql
type Category {
    categoryId: ID!
    categoryName: String
    icon: String
    products: [Product]
}

type Product {
    productId: ID!
    productName: String
    unitPrice: Float
    discount: Float
    quantity: Int
    description: String
    images: String
    status: Int
    category: Category
}

input ProductInput {
    productName: String!
    unitPrice: Float!
    discount: Float
    quantity: Int!
    description: String
    images: String
    status: Int
    categoryId: ID!
}

input CategoryInput {
    categoryName: String!
    icon: String
}

type ProductPage {
    content: [Product]
    totalPages: Int
    totalElements: Int
    currentPage: Int
    size: Int
}

type CategoryPage {
    content: [Category]
    totalPages: Int
    totalElements: Int
    currentPage: Int
    size: Int
}

type Query {
    productsPriceAsc: [Product]
    productsByCategory(categoryId: ID!): [Product]
    allCategories: [Category]
    allProducts: [Product]
    productsPaginated(name: String, categoryId: ID, page: Int!, size: Int!): ProductPage
    categoriesPaginated(name: String, page: Int!, size: Int!): CategoryPage
    productById(id: ID!): Product
    categoryById(id: ID!): Category
}

type Mutation {
    createProduct(input: ProductInput!): Product
    updateProduct(id: ID!, input: ProductInput!): Product
    deleteProduct(id: ID!): Boolean

    createCategory(input: CategoryInput!): Category
    updateCategory(id: ID!, input: CategoryInput!): Category
    deleteCategory(id: ID!): Boolean
}
```

### 2. Spring Controllers GraphQL
- **`ProductGraphQLController` (`vn.iotstar.controller.graphql`):**
  - Query `productsPriceAsc`: Trả về danh sách sản phẩm sắp xếp giá từ thấp đến cao.
  - Query `productsByCategory`: Lọc danh sách sản phẩm theo 01 `categoryId`.
  - Query `productsPaginated`: Tìm kiếm phân trang sản phẩm (trả về wrapper `ProductPage`).
  - Mutations `createProduct`, `updateProduct`, `deleteProduct`.
  - `@SchemaMapping` giải quyết linh hoạt các trường `unitPrice`, `quantity`, `images`, `category`.
- **`CategoryGraphQLController` (`vn.iotstar.controller.graphql`):**
  - Query `allCategories`, `categoryById`.
  - Query `categoriesPaginated`: Tìm kiếm phân trang danh mục (trả về wrapper `CategoryPage`).
  - Mutations `createCategory`, `updateCategory`, `deleteCategory`.

---

## 🖥 Giao Diện Demo Thymeleaf & AJAX GraphQL

1. **Trang chủ Client (`http://localhost:8081/th/home`)**:
   - Banner giao diện sang trọng.
   - Tải động danh sách các nút/badge Danh mục qua GraphQL query `allCategories`.
   - Mặc định gọi GraphQL `productsPriceAsc` render các Card sản phẩm giá tăng dần.
   - Bấm chọn danh mục bất kỳ gọi `productsByCategory(categoryId: ...)` cập nhật danh sách sản phẩm mượt mà không tải lại trang.
   - Modal xem chi tiết sản phẩm.

2. **Trang Quản lý GraphQL CRUD & Phân trang (`http://localhost:8081/th/admin/graphql-management`)**:
   - **Tab Quản lý Sản phẩm (Product):** Thanh tìm kiếm tên, dropdown lọc category, chọn page size (5, 10, 20), bảng dữ liệu phân trang, Modal Thêm/Sửa sản phẩm, nút Xóa xác nhận SweetAlert2 qua GraphQL mutation.
   - **Tab Quản lý Danh mục (Category):** Thanh tìm kiếm tên danh mục, bảng dữ liệu phân trang, Modal Thêm/Sửa danh mục, nút Xóa xác nhận SweetAlert2.

3. **GraphiQL IDE Explorer (`http://localhost:8081/graphiql`)**: Trực tiếp thử nghiệm truy vấn & mutation GraphQL trên trình duyệt.

---

## ⚡ Hệ Thống RESTful API & Giao Diện JSP Cũ

- **Trang Quản lý Category (REST AJAX JSP):** `http://localhost:8081/admin/categories/ajax`
- **Trang Quản lý Product (REST AJAX JSP):** `http://localhost:8081/admin/products/ajax`
- **Endpoints REST API (`/api/category`, `/api/product`):** Hỗ trợ CRUD RESTful API cùng cơ chế upload file.

---

## 🗄 Chuẩn Bị Cơ Sở Dữ Liệu

1. Mở SQL Server Management Studio (SSMS) hoặc Azure Data Studio.
2. Kết nối tới `localhost,1433` bằng tài khoản `sa`.
3. Chạy toàn bộ file [database.sql](database.sql) để khởi tạo CSDL `jakartaJPA` và dữ liệu mẫu.

---

## 🚀 Hướng Dẫn Build Và Khởi Chạy Ứng Dụng

Mở PowerShell tại thư mục gốc của dự án và chạy các lệnh sau:

```powershell
# 1. Cấu hình biến môi trường CSDL và Port
$env:DB_PASSWORD = 'Thanhluon@25'
$env:SERVER_PORT = '8081'

# 2. Xóa cache và đóng gói file WAR
mvn clean package -DskipTests -s .mvn\settings.xml

# 3. Khởi chạy ứng dụng
java -jar target\btap05.war
```

### Đường dẫn truy cập nghiệm thu:
- 🏠 **Trang chủ Client GraphQL AJAX:** `http://localhost:8081/th/home`
- ⚙️ **Trang Quản lý Admin GraphQL CRUD:** `http://localhost:8081/th/admin/graphql-management`
- 🚀 **GraphiQL IDE Explorer:** `http://localhost:8081/graphiql`
- 📂 **Quản lý Category (REST AJAX):** `http://localhost:8081/admin/categories/ajax`
- 📦 **Quản lý Product (REST AJAX):** `http://localhost:8081/admin/products/ajax`
- 🔑 **Đăng nhập:** `http://localhost:8081/login`

### Tài khoản Admin thử nghiệm:
- **Email:** `admin@iotstar.vn`
- **Mật khẩu:** `Admin@123`
"# BTAP07_WEB_MUC5" 
