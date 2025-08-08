# Admin Module Structure

## 📁 Cấu trúc thư mục

```
src/main/java/com/chatapp/backend/
├── admin/
│   ├── controller/
│   │   └── AdminController.java          # Dashboard & Stats
│   └── user/
│       ├── controller/
│       │   └── AdminUserController.java  # User Management APIs
│       ├── dto/
│       │   ├── UserDto.java
│       │   ├── CreateUserRequest.java
│       │   └── UpdateUserRequest.java
│       └── service/
│           └── AdminUserService.java
├── auth/
│   ├── config/
│   ├── controller/
│   ├── dto/
│   ├── jwt/
│   └── service/
├── common/
│   ├── config/
│   │   └── SwaggerConfig.java           # Swagger + Bearer Token
│   ├── constants/
│   ├── controller/
│   └── exception/
└── user/
    ├── entity/
    ├── repository/
    └── service/
```

## 🔧 Tính năng chính

### 1. **Tổ chức code rõ ràng**
- **Admin module** tách biệt hoàn toàn
- **User management** trong `admin/user/` package
- **Mỗi module** có controller, dto, service riêng
- **Dễ mở rộng** - thêm module mới chỉ cần tạo package tương ứng

### 2. **Bearer Token Authentication**
- **Swagger UI** hỗ trợ Bearer token
- **Security requirement** cho tất cả admin APIs
- **JWT token** validation tự động

### 3. **API Endpoints**

#### **Dashboard & Stats** (`/admin`)
- `GET /admin/dashboard` - Admin dashboard
- `GET /admin/stats` - System statistics

#### **User Management** (`/admin/users`)
- `GET /admin/users` - Lấy danh sách users
- `GET /admin/users/{id}` - Lấy user theo ID
- `POST /admin/users` - Tạo user mới
- `PUT /admin/users/{id}` - Cập nhật user
- `DELETE /admin/users/{id}` - Xóa user
- `PATCH /admin/users/{id}/promote` - Promote lên admin
- `PATCH /admin/users/{id}/demote` - Demote xuống user

## 🔒 Security

### **Authentication**
- Tất cả admin endpoints yêu cầu **Bearer token**
- Token được validate qua JWT filter
- Role-based access control

### **Authorization**
- `@PreAuthorize("hasRole('ADMIN')")` cho tất cả admin APIs
- Spring Security method-level security
- Custom exception handling

## 📚 Swagger Documentation

### **Bearer Token Setup**
1. Mở Swagger UI: `http://localhost:8081/swagger-ui.html`
2. Click **"Authorize"** button
3. Nhập JWT token: `Bearer <your-jwt-token>`
4. Click **"Authorize"**

### **API Groups**
- **Admin Dashboard** - Dashboard & system stats
- **Admin User Management** - User CRUD operations
- **Authentication** - Login, register, refresh token

## 🚀 Cách sử dụng

### **1. Đăng nhập để lấy token**
```bash
POST /api/v1/auth/login
{
  "usernameOrEmail": "admin",
  "password": "password"
}
```

### **2. Sử dụng token trong Swagger**
- Copy access token từ response
- Paste vào Swagger Authorize: `Bearer <token>`

### **3. Gọi admin APIs**
- Tất cả admin APIs sẽ tự động include Bearer token
- Role validation tự động

## 🔄 Mở rộng

### **Thêm module mới**
```java
// Ví dụ: Admin Product Management
src/main/java/com/chatapp/backend/admin/product/
├── controller/
│   └── AdminProductController.java
├── dto/
│   ├── ProductDto.java
│   └── CreateProductRequest.java
└── service/
    └── AdminProductService.java
```

### **Pattern**
- **Controller**: `@RequestMapping("/admin/{module}")`
- **Security**: `@PreAuthorize("hasRole('ADMIN')")`
- **Swagger**: `@SecurityRequirement(name = "Bearer Authentication")`

## ✅ Lợi ích

1. **Clean Architecture** - Tách biệt rõ ràng các concerns
2. **Scalable** - Dễ dàng thêm module mới
3. **Maintainable** - Code dễ đọc, dễ bảo trì
4. **Secure** - Bearer token authentication
5. **Documented** - Swagger documentation đầy đủ
6. **Testable** - Mỗi layer có thể test riêng biệt
