IF DB_ID(N'jakartaJPA') IS NULL CREATE DATABASE jakartaJPA;
GO
USE jakartaJPA;
GO
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS videos;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS users;
GO
CREATE TABLE categories (
  categoryId INT IDENTITY(1,1) PRIMARY KEY,
  categoryname NVARCHAR(255) NOT NULL,
  images NVARCHAR(255) NULL,
  status INT NOT NULL CONSTRAINT DF_categories_status DEFAULT 1
);
CREATE TABLE users (
  id INT IDENTITY(1,1) PRIMARY KEY,
  username NVARCHAR(255) NOT NULL,
  email NVARCHAR(255) NOT NULL UNIQUE,
  password NVARCHAR(255) NOT NULL,
  fullname NVARCHAR(255) NOT NULL,
  phone NVARCHAR(20) NULL,
  images NVARCHAR(255) NULL,
  role INT NOT NULL CONSTRAINT DF_users_role DEFAULT 0,
  status INT NOT NULL CONSTRAINT DF_users_status DEFAULT 1,
  otp_code VARCHAR(10) NULL,
  otp_expiry DATETIME NULL
);
CREATE TABLE videos (
  videoId VARCHAR(255) PRIMARY KEY, active BIT NOT NULL, description NVARCHAR(MAX), poster NVARCHAR(255), title NVARCHAR(255), views INT NOT NULL DEFAULT 0,
  categoryId INT NULL FOREIGN KEY REFERENCES categories(categoryId)
);
CREATE TABLE products (
  productId INT IDENTITY(1,1) PRIMARY KEY,
  productName NVARCHAR(255) NOT NULL, description NVARCHAR(MAX) NULL,
  price FLOAT NOT NULL, stock INT NOT NULL DEFAULT 0, images NVARCHAR(255) NULL, status INT NOT NULL DEFAULT 1,
  categoryId INT NULL FOREIGN KEY REFERENCES categories(categoryId)
);
GO
INSERT INTO categories(categoryname, images, status) VALUES
(N'Điện thoại', N'https://placehold.co/400x250/0d6efd/ffffff?text=Phone', 1),
(N'Laptop', N'https://placehold.co/400x250/198754/ffffff?text=Laptop', 1),
(N'Phụ kiện', N'https://placehold.co/400x250/6f42c1/ffffff?text=Accessory', 1);
INSERT INTO products(productName,description,price,stock,images,status,categoryId) VALUES
(N'iPhone 15 Pro',N'Điện thoại Apple',30000000,20,N'https://placehold.co/400x250/0d6efd/ffffff?text=iPhone',1,1),
(N'MacBook Air M3',N'Laptop mỏng nhẹ',29000000,15,N'https://placehold.co/400x250/198754/ffffff?text=MacBook',1,2),
(N'AirPods Pro',N'Tai nghe không dây',5500000,50,N'https://placehold.co/400x250/6f42c1/ffffff?text=AirPods',1,3);
GO
-- Tài khoản mẫu được Spring Boot tự tạo ở lần chạy đầu:
-- admin@iotstar.vn / Admin@123
-- user@iotstar.vn  / User@123
