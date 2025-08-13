# Controller Cleanup Guide

## Tổng quan

Đã refactor tất cả controllers để có code clean, gọn gàng và nhất quán hơn. Sử dụng utility chung `ResponseUtils` để tạo paginated responses và đã loại bỏ các file duplicate.

## Các cải tiến đã thực hiện

### 1. **Tạo ResponseUtils Utility (Consolidated)**
- **File**: `src/main/java/com/chatapp/backend/common/utils/ResponseUtils.java`
- **Mục đích**: Tạo paginated responses với format nhất quán
- **Lợi ích**: Tái sử dụng code, dễ maintain, không duplicate

### 2. **Loại bỏ Files Duplicate**
- ✅ **Xóa PaginatedResponse.java** - Không còn sử dụng
- ✅ **Xóa PaginationUtils.java** - Đã gộp vào ResponseUtils
- ✅ **Cập nhật API_RESPONSE_TEMPLATE.md** - Phản ánh pattern mới

### 3. **Clean up AdminUserController**
- ✅ Loại bỏ helper method local
- ✅ Sử dụng ResponseUtils
- ✅ Code ngắn gọn hơn
- ✅ Parameters trên cùng một dòng

### 4. **Clean up UserController**
- ✅ Sử dụng ResponseUtils thay vì PaginatedResponse
- ✅ Format code nhất quán
- ✅ Response format đúng chuẩn

### 5. **Clean up MessageController**
- ✅ Thêm proper API response structure
- ✅ Sử dụng ResponseUtils cho pagination
- ✅ Thêm proper annotations và documentation
- ✅ Consistent parameter handling

### 6. **Cập nhật MessageConstants**
- ✅ Thêm constants cho message management
- ✅ Tránh hardcode strings

## Cách sử dụng ResponseUtils

### Paginated Response với custom message
```java
@GetMapping("/paginated")
public ResponseEntity<ApiResponse<List<UserDto>>> getPaginatedUsers(
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size) {
    
    Page<UserDto> pageResult = userService.getUsersPaginated(page, size);
    return ResponseEntity.ok(ResponseUtils.createPaginatedResponse(
        pageResult, page, size, MessageConstants.USERS_RETRIEVED));
}
```

### Paginated Response với default message
```java
@GetMapping("/paginated")
public ResponseEntity<ApiResponse<List<UserDto>>> getPaginatedUsers(
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size) {
    
    Page<UserDto> pageResult = userService.getUsersPaginated(page, size);
    return ResponseEntity.ok(ResponseUtils.createPaginatedResponse(pageResult, page, size));
}
```

### Paginated Response với Page object
```java
@GetMapping("/paginated")
public ResponseEntity<ApiResponse<List<UserDto>>> getPaginatedUsers(
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size) {
    
    Page<UserDto> pageResult = userService.getUsersPaginated(page, size);
    return ResponseEntity.ok(ResponseUtils.createPaginatedResponse(pageResult, "Custom message"));
}
```

### Tạo PaginationInfo riêng biệt
```java
// Từ Spring Data Page
ApiResponse.PaginationInfo pagination = ResponseUtils.createPaginationInfo(page, currentPage, pageSize);

// Từ Page object
ApiResponse.PaginationInfo pagination = ResponseUtils.createPaginationInfo(page);

// Tạo thủ công
ApiResponse.PaginationInfo pagination = ResponseUtils.createPaginationInfo(page, limit, total);
```

## Response Format

Tất cả paginated responses sẽ có format:
```json
{
  "success": true,
  "code": 200,
  "message": "Lấy danh sách thành công",
  "timestamp": "2025-08-13T09:57:30.347Z",
  "data": [
    {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "username": "john_doe",
      "email": "john.doe@example.com"
    }
  ],
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

## Best Practices

### 1. **Controller Structure**
```java
@RestController
@RequestMapping("/api/v1/resource")
@RequiredArgsConstructor
@Tag(name = "Resource", description = "Resource management APIs")
public class ResourceController {
    
    private final ResourceService resourceService;
    
    @GetMapping("/paginated")
    @Operation(summary = "Get paginated resources", description = "Get paginated list of resources")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success")
    public ResponseEntity<ApiResponse<List<ResourceDto>>> getPaginatedResources(
            @Parameter(description = "Page number", example = "1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Page size", example = "10") @RequestParam(defaultValue = "10") int size) {
        
        Page<ResourceDto> pageResult = resourceService.getPaginated(page, size);
        return ResponseEntity.ok(ResponseUtils.createPaginatedResponse(
            pageResult, page, size, MessageConstants.RESOURCES_RETRIEVED));
    }
}
```

### 2. **Service Structure**
```java
@Service
@RequiredArgsConstructor
public class ResourceService {
    
    private final ResourceRepository resourceRepository;
    
    public Page<ResourceDto> getPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return resourceRepository.findAll(pageable).map(this::mapToDto);
    }
    
    private ResourceDto mapToDto(Resource resource) {
        return ResourceDto.builder()
                .id(resource.getId())
                .name(resource.getName())
                .build();
    }
}
```

### 3. **Constants**
- Luôn sử dụng `MessageConstants` thay vì hardcode strings
- Thêm constants mới khi cần thiết

## Files đã được cập nhật

1. ✅ `ResponseUtils.java` - Utility mới (consolidated)
2. ✅ `AdminUserController.java` - Clean up
3. ✅ `UserController.java` - Clean up  
4. ✅ `MessageController.java` - Clean up
5. ✅ `MessageConstants.java` - Thêm constants mới
6. ✅ `API_RESPONSE_TEMPLATE.md` - Cập nhật pattern mới

## Files đã được xóa

1. ❌ `PaginatedResponse.java` - Không còn sử dụng
2. ❌ `PaginationUtils.java` - Đã gộp vào ResponseUtils

## Lợi ích

- 🎯 **Nhất quán**: Tất cả controllers có format giống nhau
- 🔧 **Dễ maintain**: Code clean, tách biệt rõ ràng
- ♻️ **Tái sử dụng**: ResponseUtils có thể dùng cho tất cả controllers
- 📚 **Dễ hiểu**: Code ngắn gọn, dễ đọc
- 🧪 **Dễ test**: Mỗi method có trách nhiệm rõ ràng
- 🗑️ **Không duplicate**: Loại bỏ code trùng lặp
- 📦 **Consolidated**: Tất cả pagination logic trong một file

## Migration Guide

### Từ PaginatedResponse cũ
```java
// Cũ
return ResponseEntity.ok(PaginatedResponse.of(page, message));

// Mới
return ResponseEntity.ok(ResponseUtils.createPaginatedResponse(page, message));
```

### Từ PaginationUtils cũ
```java
// Cũ
ApiResponse.PaginationInfo pagination = PaginationUtils.createPaginationInfo(page);

// Mới
ApiResponse.PaginationInfo pagination = ResponseUtils.createPaginationInfo(page);
```
