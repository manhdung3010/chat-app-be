# API Response Template

## Cấu trúc Response Chuẩn

```json
{
  "success": true,
  "code": 200,
  "message": "Thao tác thành công",
  "timestamp": "2024-01-01T12:00:00",
  "data": {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "username": "john_doe",
    "email": "john@example.com"
  },
  "pagination": {
    "page": 1,
    "limit": 10,
    "total": 100,
    "totalPages": 10,
    "hasNext": true,
    "hasPrevious": false
  }
}
```

## Cách sử dụng

### Response thành công
```java
// 200 OK
return ResponseEntity.ok(ApiResponse.success(data, "Thao tác thành công"));

// 201 Created
return ResponseEntity.status(HttpStatus.CREATED)
    .body(ApiResponse.created(data, "Tạo mới thành công"));

// 204 No Content
return ResponseEntity.status(HttpStatus.NO_CONTENT)
    .body(ApiResponse.noContent("Xóa thành công"));
```

### Response lỗi
```java
// 400 Bad Request
return ResponseEntity.badRequest()
    .body(ApiResponse.badRequest("Dữ liệu không hợp lệ"));

// 401 Unauthorized
return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
    .body(ApiResponse.unauthorized("Thông tin đăng nhập không chính xác"));

// 404 Not Found
return ResponseEntity.status(HttpStatus.NOT_FOUND)
    .body(ApiResponse.notFound("Không tìm thấy dữ liệu"));
```

### Response với pagination
```java
// Sử dụng Spring Data Page
Page<UserDto> userPage = userService.getUsers(pageable);
return ResponseEntity.ok(PaginatedResponse.of(userPage, "Lấy danh sách thành công"));
```

## Files chính
- `ApiResponse.java` - Template chính
- `PaginationUtils.java` - Utility cho pagination
- `PaginatedResponse.java` - Helper cho response có pagination
- `MessageConstants.java` - Constants cho tất cả messages (tránh hardcode)
- `PackageConstants.java` - Constants cho package names (tránh hardcode)

## Lợi ích

1. **Tính nhất quán**: Tất cả API đều có cấu trúc response giống nhau
2. **Dễ hiểu**: Frontend có thể xử lý response một cách thống nhất
3. **Thông tin đầy đủ**: Bao gồm status, message, timestamp và pagination
4. **Linh hoạt**: Có thể tùy chỉnh message và data theo từng endpoint
5. **Type-safe**: Sử dụng generics để đảm bảo type safety
6. **Không hardcode**: Sử dụng constants cho tất cả messages và package names
