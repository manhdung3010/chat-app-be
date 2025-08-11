# Hướng dẫn sử dụng Flyway cho Database Migration

## Tại sao sử dụng Flyway?

Flyway giúp bạn:
- **Bảo toàn dữ liệu**: Không bao giờ mất dữ liệu khi thay đổi schema
- **Version control**: Theo dõi được lịch sử thay đổi database
- **Team collaboration**: Đồng bộ schema giữa các môi trường
- **Rollback**: Có thể rollback về version cũ nếu cần

## Cấu trúc thư mục

```
src/main/resources/db/migration/
├── V1__Create_users_table.sql          # Migration đầu tiên
├── V2__Add_phone_number_to_users.sql   # Migration thêm cột
└── V3__Add_new_feature.sql            # Migration tiếp theo
```

## Quy tắc đặt tên file migration

```
V{version}__{description}.sql
```

Ví dụ:
- `V1__Create_users_table.sql`
- `V2__Add_phone_number_to_users.sql`
- `V3__Create_messages_table.sql`

## Cách thêm cột mới (không mất dữ liệu)

### Bước 1: Tạo file migration mới

Tạo file `V3__Add_new_column.sql` trong thư mục `src/main/resources/db/migration/`

### Bước 2: Viết SQL migration

```sql
-- Migration: V3__Add_new_column.sql
-- Description: Thêm cột mới vào bảng users

-- Thêm cột mới (luôn để NULL để không ảnh hưởng dữ liệu cũ)
ALTER TABLE users ADD COLUMN new_column VARCHAR(100);

-- Tạo index nếu cần
CREATE INDEX idx_users_new_column ON users(new_column);

-- Thêm comment
COMMENT ON COLUMN users.new_column IS 'Mô tả cột mới';
```

### Bước 3: Cập nhật Entity Java

```java
@Entity
@Table(name = "users")
public class User {
    // ... các field khác
    
    @Column(name = "new_column")
    private String newColumn;
    
    // ... getters, setters
}
```

### Bước 4: Chạy migration

Khi khởi động ứng dụng, Flyway sẽ tự động chạy migration mới.

## Các lệnh Flyway hữu ích

### Xem trạng thái migration
```bash
mvn flyway:info
```

### Chạy migration thủ công
```bash
mvn flyway:migrate
```

### Xóa database và tạo lại
```bash
mvn flyway:clean
mvn flyway:migrate
```

## Best Practices

1. **Luôn backup database** trước khi chạy migration
2. **Test migration** trên môi trường development trước
3. **Không sửa file migration đã chạy** - tạo file mới thay vì sửa file cũ
4. **Viết migration có thể rollback** nếu cần
5. **Sử dụng transaction** cho migration phức tạp

## Ví dụ migration phức tạp

```sql
-- Migration: V4__Complex_changes.sql
-- Description: Thực hiện thay đổi phức tạp

BEGIN;

-- Thêm cột mới
ALTER TABLE users ADD COLUMN status VARCHAR(20) DEFAULT 'ACTIVE';

-- Cập nhật dữ liệu cũ
UPDATE users SET status = 'ACTIVE' WHERE status IS NULL;

-- Thêm constraint
ALTER TABLE users ALTER COLUMN status SET NOT NULL;

-- Tạo index
CREATE INDEX idx_users_status ON users(status);

COMMIT;
```

## Troubleshooting

### Lỗi: "Migration checksum mismatch"
- Xóa bảng `flyway_schema_history` và chạy lại migration
- Hoặc sử dụng `mvn flyway:repair`

### Lỗi: "Migration version already applied"
- Kiểm tra bảng `flyway_schema_history` trong database
- Xóa record tương ứng nếu cần

### Lỗi: "Table already exists"
- Sử dụng `CREATE TABLE IF NOT EXISTS` hoặc `DROP TABLE IF EXISTS`

