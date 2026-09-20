# Bài Tập 07 - Web Nâng Cao: RESTful API & AJAX CRUD, Search & Phân Trang

**Sinh viên thực hiện:** Trần Thanh Luôn  
**Mã số sinh viên:** 24110280  
**Repository GitHub:** [https://github.com/TLuon/BTAP07_WEB_MUC4](https://github.com/TLuon/BTAP07_WEB_MUC4)

---

## 📖 Giới Thiệu Dự Án

Dự án được triển khai trên nền tảng **Spring Boot & Frontend JSP/AJAX**, hoàn thiện toàn bộ các quy chuẩn về **RESTful API kết hợp AJAX** cho các thao tác **CRUD và Tìm kiếm có Phân trang (Search & Pagination)** trên 2 bảng dữ liệu chính: `Category` (Danh mục) và `Product` (Sản phẩm).

---

## 🛠 Công Nghệ Sử Dụng

- **Backend:** Java 21, Spring Boot 4.0.8, Spring MVC, Spring Data JPA, Spring Security.
- **RESTful API Architecture:** 
  - Controllers API: `vn.iotstar.controllers.api`
  - Data Wrapper: `Response(Boolean status, String message, Object body)`
  - Storage Service: `IStorageService` (`StorageServiceImpl`) hỗ trợ upload và lưu trữ file icon/image.
- **Database:** Microsoft SQL Server (`jakartaJPA` database).
- **Frontend AJAX & Giao diện:** jQuery 3.6.4+, Bootstrap 5.3.3, Glassmorphism & Gradient Design System (`app.css`), Modal Bootstrap, `FormData` multipart/form-data.

---

## ⚡ Chi Tiết Hệ Thống RESTful API

### 1. Category API (`/api/category`)
- `GET /api/category/searchPaginated?name={name}&page={page}&size={size}&sort={sortField}`: Tìm kiếm phân trang danh mục.
- `GET /api/category`: Lấy danh sách tất cả danh mục.
- `POST /api/category/getCategory?id={id}`: Lấy chi tiết danh mục theo ID.
- `POST /api/category/addCategory`: Thêm danh mục mới (hỗ trợ upload icon file `MultipartFile`).
- `PUT /api/category/updateCategory`: Cập nhật thông tin danh mục (giữ nguyên icon cũ nếu không đính kèm file mới).
- `DELETE /api/category/deleteCategory?categoryId={id}`: Xóa danh mục.

### 2. Product API (`/api/product`)
- `GET /api/product/searchPaginated?name={name}&categoryId={catId}&page={page}&size={size}&sort={sortField}`: Tìm kiếm phân trang sản phẩm theo tên & lọc danh mục.
- `GET /api/product`: Lấy danh sách tất cả sản phẩm.
- `POST /api/product/getProduct?id={id}`: Lấy thông tin chi tiết 1 sản phẩm.
- `POST /api/product/addProduct`: Thêm mới sản phẩm (upload `imageFile`, liên kết `Category`, `unitPrice`, `discount`, `quantity`, `description`, `status`).
- `PUT /api/product/updateProduct`: Cập nhật thông tin sản phẩm và cập nhật ảnh mới.
- `DELETE /api/product/deleteProduct?productId={id}`: Xóa sản phẩm.

---

## 🖥 Giao Diện AJAX Frontend Không Tải Lại Trang

- **Trang Quản lý Danh mục (AJAX):** `http://localhost:8081/admin/categories/ajax`
- **Trang Quản lý Sản phẩm (AJAX):** `http://localhost:8081/admin/products/ajax`

### Chức năng nổi bật phía Frontend:
1. **Render dữ liệu động:** Dữ liệu tự động tải và hiển thị vào thẻ `<table>` thông qua `loadData()` bằng jQuery AJAX.
2. **Tìm kiếm & Thay đổi số lượng hiển thị (`size: 5, 10, 20`):** Lọc dữ liệu tức thì không cần reload trang web.
3. **Phân trang động:** Các nút Previous, Số trang (`1, 2, 3...`), Next phản hồi thời gian thực.
4. **Modal Form Thêm / Sửa:** Tích hợp `FormData` gửi dữ liệu dạng `multipart/form-data` kèm xem trước ảnh (Image Preview).
5. **Ủy quyền sự kiện Xóa:** Thao tác nút Xóa hiển thị Confirm alert, gọi API `DELETE` và xóa hàng khỏi bảng mượt mà.

---

## 🗄 Chuẩn Bị Cơ Sở Dữ Liệu

1. Mở SQL Server Management Studio (SSMS) hoặc Azure Data Studio.
2. Kết nối tới `localhost,1433` bằng tài khoản `sa`.
3. Chạy toàn bộ file [database.sql](database.sql) để tạo CSDL `jakartaJPA`, các bảng và dữ liệu mẫu.

---

## 🚀 Hướng Dẫn Build Và Chạy Ứng Dụng

Mở PowerShell tại thư mục gốc của dự án và chạy các lệnh sau:

```powershell
# 1. Cấu hình biến môi trường CSDL và Port
$env:DB_PASSWORD = 'Thanhluon@25'
$env:SERVER_PORT = '8081'

# 2. Xóa cache và Build file WAR
mvn clean package -DskipTests -s .mvn\settings.xml

# 3. Khởi chạy ứng dụng
java -jar target\btap05.war
```

### Đường dẫn truy cập ứng dụng:
- **Trang chủ:** `http://localhost:8081/`
- **Quản lý Category (AJAX):** `http://localhost:8081/admin/categories/ajax`
- **Quản lý Product (AJAX):** `http://localhost:8081/admin/products/ajax`
- **Đăng nhập:** `http://localhost:8081/login`

### Tài khoản Admin thử nghiệm:
- **Email:** `admin@iotstar.vn`
- **Mật khẩu:** `Admin@123`
