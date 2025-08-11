-- Migration: V2__Add_phone_number_to_users.sql
-- Description: Thêm cột phone_number vào bảng users
-- Đây là ví dụ về cách thêm cột mới mà không mất dữ liệu

-- Thêm cột phone_number (có thể NULL để không ảnh hưởng đến dữ liệu cũ)
ALTER TABLE users ADD COLUMN phone_number VARCHAR(20);

-- Tạo index cho cột mới (tùy chọn, nếu cần tìm kiếm theo số điện thoại)
CREATE INDEX idx_users_phone_number ON users(phone_number);

-- Thêm comment để mô tả cột
COMMENT ON COLUMN users.phone_number IS 'Số điện thoại của người dùng (tùy chọn)';

