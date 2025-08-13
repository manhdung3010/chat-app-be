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

### Response với pagination (Sử dụng ResponseUtils)
```java
// Paginated response với custom message
Page<UserDto> userPage = userService.getUsers(pageable);
return ResponseEntity.ok(ResponseUtils.createPaginatedResponse(
    userPage, page, size, MessageConstants.USERS_RETRIEVED));

// Paginated response với default message
return ResponseEntity.ok(ResponseUtils.createPaginatedResponse(userPage, page, size));

// Paginated response với Page object
return ResponseEntity.ok(ResponseUtils.createPaginatedResponse(userPage, "Custom message"));
```

### Tạo pagination info riêng biệt
```java
// Từ Spring Data Page
ApiResponse.PaginationInfo pagination = ResponseUtils.createPaginationInfo(page, currentPage, pageSize);

// Từ Page object
ApiResponse.PaginationInfo pagination = ResponseUtils.createPaginationInfo(page);

// Tạo thủ công
ApiResponse.PaginationInfo pagination = ResponseUtils.createPaginationInfo(page, limit, total);
```

## Files chính
- `ApiResponse.java` - Template chính cho response
- `ResponseUtils.java` - Utility cho pagination và response creation
- `MessageConstants.java` - Constants cho tất cả messages (tránh hardcode)
- `AppConstants.java` - Constants cho package names và paths

## Pattern mới (Clean & Consistent)

### Controller Pattern
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

### Service Pattern
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

## Lợi ích

1. **Tính nhất quán**: Tất cả API đều có cấu trúc response giống nhau
2. **Dễ maintain**: Sử dụng ResponseUtils thống nhất
3. **Tái sử dụng**: Không duplicate code
4. **Clean code**: Pattern rõ ràng, dễ hiểu
5. **Type safety**: Sử dụng generics cho type safety
6. **Flexibility**: Nhiều overloaded methods cho các use cases khác nhau

## Migration từ PaginatedResponse

Nếu có code cũ sử dụng `PaginatedResponse`, thay thế bằng:

```java
// Cũ
return ResponseEntity.ok(PaginatedResponse.of(page, message));

// Mới
return ResponseEntity.ok(ResponseUtils.createPaginatedResponse(page, message));
```
