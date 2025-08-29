
# E-Commerce Admin Dashboard API

專案簡述:

本專案旨在開發一個電子商務後台管理系統的 API，其功能與介面參考 react-admin-demo。後端將使用 Java Spring Boot 框架搭配 MySQL 資料庫進行開發，並透過 Swagger UI 提供清晰的 API 文件。

🛠️ 技術棧 (Technology Stack)
後端框架 (Backend Framework): Spring Boot

資料庫 (Database): MySQL

API 文件 (API Documentation): Swagger

✨ 核心功能 (Features)
本專案 API 系統將涵蓋以下四大模組：

1. 銷售模組 (Sales)
   - 訂單管理 (Commands)
    - 發票管理 (Invoices)

2. 產品模組 (Products)
    - 產品管理 (Products)
    - 產品類別管理 (Categories)

3. 用戶模組 (Users)
   - 用戶管理 (Customers)
   - 用戶標籤管理 (Segments)

4. 評論模組 (Reviews)
    - 評論管理 (Reviews)

 初始化專案

 SQL連線設定

 預先建立本地資料庫

```
create database commerce_db;
```

DB連線資訊
```
application.properties

spring.application.name=demo
spring.datasource.url=jdbc:mysql://localhost:3306/commerce_db
spring.datasource.username=<username>
spring.datasource.password=<password>
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```
