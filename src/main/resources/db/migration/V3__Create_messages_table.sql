-- Migration: V3__Create_messages_table.sql
-- Description: Tạo bảng messages cho chức năng chat

CREATE TABLE messages (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    content TEXT NOT NULL,
    sender_id UUID NOT NULL,
    receiver_id UUID,
    room_id UUID,
    message_type VARCHAR(20) NOT NULL DEFAULT 'TEXT' CHECK (message_type IN ('TEXT', 'IMAGE', 'FILE', 'AUDIO', 'VIDEO')),
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    -- Foreign key constraints
    CONSTRAINT fk_messages_sender FOREIGN KEY (sender_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_messages_receiver FOREIGN KEY (receiver_id) REFERENCES users(id) ON DELETE CASCADE,
    
    -- Check constraint: phải có receiver_id hoặc room_id
    CONSTRAINT chk_messages_target CHECK (receiver_id IS NOT NULL OR room_id IS NOT NULL)
);

-- Tạo indexes để tối ưu hiệu suất truy vấn
CREATE INDEX idx_messages_sender_id ON messages(sender_id);
CREATE INDEX idx_messages_receiver_id ON messages(receiver_id);
CREATE INDEX idx_messages_room_id ON messages(room_id);
CREATE INDEX idx_messages_created_at ON messages(created_at);
CREATE INDEX idx_messages_is_read ON messages(is_read);

-- Tạo composite index cho truy vấn tin nhắn giữa 2 user
CREATE INDEX idx_messages_sender_receiver ON messages(sender_id, receiver_id);

-- Tạo trigger để tự động cập nhật updated_at
CREATE TRIGGER update_messages_updated_at 
    BEFORE UPDATE ON messages 
    FOR EACH ROW 
    EXECUTE FUNCTION update_updated_at_column();

-- Thêm comments để mô tả bảng và các cột
COMMENT ON TABLE messages IS 'Bảng lưu trữ tin nhắn chat';
COMMENT ON COLUMN messages.content IS 'Nội dung tin nhắn';
COMMENT ON COLUMN messages.sender_id IS 'ID người gửi tin nhắn';
COMMENT ON COLUMN messages.receiver_id IS 'ID người nhận tin nhắn (NULL nếu là group chat)';
COMMENT ON COLUMN messages.room_id IS 'ID phòng chat (NULL nếu là private chat)';
COMMENT ON COLUMN messages.message_type IS 'Loại tin nhắn: TEXT, IMAGE, FILE, AUDIO, VIDEO';
COMMENT ON COLUMN messages.is_read IS 'Trạng thái đã đọc tin nhắn';

