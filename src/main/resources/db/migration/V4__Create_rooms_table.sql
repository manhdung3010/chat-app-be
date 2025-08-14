-- Migration: V4__Create_rooms_table.sql
-- Description: Tạo bảng rooms và các bảng liên quan cho chức năng quản lý phòng chat

-- Tạo bảng rooms
CREATE TABLE rooms (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    avatar_url VARCHAR(500),
    room_type VARCHAR(20) NOT NULL DEFAULT 'GROUP' CHECK (room_type IN ('PRIVATE', 'GROUP', 'CHANNEL')),
    created_by UUID NOT NULL,
    is_private BOOLEAN NOT NULL DEFAULT FALSE,
    max_members INTEGER,
    current_member_count INTEGER NOT NULL DEFAULT 0,
    last_message_at TIMESTAMP(6),
    created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    -- Foreign key constraints
    CONSTRAINT fk_rooms_created_by FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE CASCADE
);

-- Tạo bảng room_members (junction table)
CREATE TABLE room_members (
    room_id UUID NOT NULL,
    user_id UUID NOT NULL,
    joined_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    -- Primary key
    CONSTRAINT pk_room_members PRIMARY KEY (room_id, user_id),
    
    -- Foreign key constraints
    CONSTRAINT fk_room_members_room_id FOREIGN KEY (room_id) REFERENCES rooms(id) ON DELETE CASCADE,
    CONSTRAINT fk_room_members_user_id FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Tạo bảng room_admins (junction table)
CREATE TABLE room_admins (
    room_id UUID NOT NULL,
    user_id UUID NOT NULL,
    assigned_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    -- Primary key
    CONSTRAINT pk_room_admins PRIMARY KEY (room_id, user_id),
    
    -- Foreign key constraints
    CONSTRAINT fk_room_admins_room_id FOREIGN KEY (room_id) REFERENCES rooms(id) ON DELETE CASCADE,
    CONSTRAINT fk_room_admins_user_id FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Tạo indexes để tối ưu hiệu suất truy vấn
CREATE INDEX idx_rooms_created_by ON rooms(created_by);
CREATE INDEX idx_rooms_room_type ON rooms(room_type);
CREATE INDEX idx_rooms_is_private ON rooms(is_private);
CREATE INDEX idx_rooms_last_message_at ON rooms(last_message_at);
CREATE INDEX idx_rooms_created_at ON rooms(created_at);

CREATE INDEX idx_room_members_room_id ON room_members(room_id);
CREATE INDEX idx_room_members_user_id ON room_members(user_id);
CREATE INDEX idx_room_members_joined_at ON room_members(joined_at);

CREATE INDEX idx_room_admins_room_id ON room_admins(room_id);
CREATE INDEX idx_room_admins_user_id ON room_admins(user_id);

-- Tạo trigger để tự động cập nhật updated_at
CREATE TRIGGER update_rooms_updated_at 
    BEFORE UPDATE ON rooms 
    FOR EACH ROW 
    EXECUTE FUNCTION update_updated_at_column();

-- Tạo trigger để tự động cập nhật current_member_count
CREATE OR REPLACE FUNCTION update_room_member_count()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'INSERT' THEN
        UPDATE rooms 
        SET current_member_count = current_member_count + 1 
        WHERE id = NEW.room_id;
        RETURN NEW;
    ELSIF TG_OP = 'DELETE' THEN
        UPDATE rooms 
        SET current_member_count = current_member_count - 1 
        WHERE id = OLD.room_id;
        RETURN OLD;
    END IF;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_room_member_count_trigger
    AFTER INSERT OR DELETE ON room_members
    FOR EACH ROW
    EXECUTE FUNCTION update_room_member_count();

-- Thêm comments để mô tả bảng và các cột
COMMENT ON TABLE rooms IS 'Bảng lưu trữ thông tin phòng chat';
COMMENT ON COLUMN rooms.name IS 'Tên phòng chat';
COMMENT ON COLUMN rooms.description IS 'Mô tả phòng chat';
COMMENT ON COLUMN rooms.avatar_url IS 'URL avatar của phòng';
COMMENT ON COLUMN rooms.room_type IS 'Loại phòng: PRIVATE, GROUP, CHANNEL';
COMMENT ON COLUMN rooms.created_by IS 'ID người tạo phòng';
COMMENT ON COLUMN rooms.is_private IS 'Phòng có phải private không';
COMMENT ON COLUMN rooms.max_members IS 'Số lượng thành viên tối đa';
COMMENT ON COLUMN rooms.current_member_count IS 'Số lượng thành viên hiện tại';
COMMENT ON COLUMN rooms.last_message_at IS 'Thời gian tin nhắn cuối cùng';

COMMENT ON TABLE room_members IS 'Bảng quan hệ nhiều-nhiều giữa rooms và users (thành viên)';
COMMENT ON TABLE room_admins IS 'Bảng quan hệ nhiều-nhiều giữa rooms và users (admin)';

