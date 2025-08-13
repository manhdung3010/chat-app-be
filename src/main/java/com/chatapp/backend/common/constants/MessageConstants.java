package com.chatapp.backend.common.constants;

/**
 * Constants for API response messages
 */
public final class MessageConstants {
    
    // Success messages
    public static final String SUCCESS_OPERATION = "Thao tác thành công";
    public static final String SUCCESS_CREATED = "Tạo mới thành công";
    public static final String SUCCESS_DELETED = "Xóa thành công";
    public static final String SUCCESS_UPDATED = "Cập nhật thành công";
    public static final String SUCCESS_RETRIEVED = "Lấy dữ liệu thành công";
    
    // Authentication messages
    public static final String AUTH_REGISTER_SUCCESS = "Đăng ký thành công";
    public static final String AUTH_LOGIN_SUCCESS = "Đăng nhập thành công";
    public static final String AUTH_REFRESH_SUCCESS = "Làm mới token thành công";
    public static final String AUTH_INVALID_CREDENTIALS = "Thông tin đăng nhập không chính xác";
    
    // User management messages
    public static final String USER_CREATED = "Tạo người dùng thành công";
    public static final String USER_UPDATED = "Cập nhật người dùng thành công";
    public static final String USER_DELETED = "Xóa người dùng thành công";
    public static final String USER_RETRIEVED = "Lấy thông tin người dùng thành công";
    public static final String USERS_RETRIEVED = "Lấy danh sách người dùng thành công";
    public static final String USER_PROMOTED = "Thăng cấp người dùng thành công";
    public static final String USER_DEMOTED = "Hạ cấp người dùng thành công";
    
    // Message management messages
    public static final String MESSAGES_RETRIEVED = "Lấy tin nhắn thành công";
    public static final String MESSAGE_DELETED = "Xóa tin nhắn thành công";
    public static final String UNREAD_COUNT_RETRIEVED = "Lấy số tin nhắn chưa đọc thành công";
    public static final String UNREAD_MESSAGES_RETRIEVED = "Lấy danh sách tin nhắn chưa đọc thành công";
    public static final String LATEST_MESSAGES_RETRIEVED = "Lấy tin nhắn gần nhất thành công";
    
    // Error messages
    public static final String ERROR_VALIDATION = "Dữ liệu không hợp lệ";
    public static final String ERROR_NOT_FOUND = "Không tìm thấy dữ liệu";
    public static final String ERROR_SERVER = "Lỗi hệ thống không mong muốn";
    public static final String ERROR_UNAUTHORIZED = "Không có quyền truy cập";
    public static final String ERROR_CONFLICT = "Dữ liệu đã tồn tại";
    public static final String ERROR_USERNAME_EXISTS = "Username already exists";
    public static final String ERROR_EMAIL_EXISTS = "Email already exists";
    
    // Pagination messages
    public static final String PAGINATION_SUCCESS = "Lấy dữ liệu thành công";
    
    private MessageConstants() {
        // Prevent instantiation
    }
}
