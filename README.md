# BTap05 – Spring Boot 4 + JSP/JSTL

Ứng dụng đã được chuyển đổi sang Spring Boot 4.0.8, Spring MVC, Spring Data JPA, Spring Security, JSP/JSTL và SQL Server. Đóng gói là **executable WAR** vì Spring Boot không hỗ trợ JSP trong executable JAR.

## Chuẩn bị cơ sở dữ liệu

Mở SQL Server Management Studio/Azure Data Studio, kết nối `localhost,1433` bằng tài khoản `sa`, rồi chạy toàn bộ file [database.sql](database.sql). Script sẽ tạo database `jakartaJPA`, các bảng và dữ liệu Product/Category mẫu.

## Build và chạy

Từ PowerShell tại thư mục dự án:

```powershell
$env:DB_PASSWORD = 'mat-khau-SQL-Server-cua-ban'
mvn package -DskipTests -s .mvn\settings.xml
java -jar target\btap05.war
```

Mở `http://localhost:8080`.

Tài khoản được ứng dụng tự tạo tại lần khởi chạy đầu:

| Loại | Email | Mật khẩu |
|---|---|---|
| Admin | `admin@iotstar.vn` | `Admin@123` |
| User | `user@iotstar.vn` | `User@123` |

## Chức năng

- Đăng ký, xác thực OTP (mã hiển thị trên giao diện demo), quên/đặt lại mật khẩu, đăng nhập, hồ sơ, đăng xuất.
- Spring Security và BCrypt; chỉ tài khoản `role = 1` vào được `/admin/**`.
- Admin CRUD Category và Product; tìm theo tên, lọc trạng thái.
- Admin quản lý User; tìm theo username/email/họ tên, lọc quyền/trạng thái, chỉnh sửa và **khóa** tài khoản (không xóa dữ liệu); không thể khóa chính admin đang đăng nhập.
- Không xóa được Category nếu còn Product liên kết.

Ảnh upload được lưu tại thư mục `uploads` cạnh nơi chạy ứng dụng; Category cũng chấp nhận URL ảnh.
