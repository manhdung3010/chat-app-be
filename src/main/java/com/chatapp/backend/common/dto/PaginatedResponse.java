package com.chatapp.backend.common.dto;

import com.chatapp.backend.common.constants.MessageConstants;
import com.chatapp.backend.common.utils.PaginationUtils;
import org.springframework.data.domain.Page;

/**
 * Helper class to create paginated responses
 */
public class PaginatedResponse {
    
    public static <T> ApiResponse<Page<T>> of(Page<T> page, String message) {
        return ApiResponse.<Page<T>>builder()
                .success(true)
                .code(200)
                .message(message)
                .data(page)
                .pagination(PaginationUtils.createPaginationInfo(page))
                .build();
    }
    
    public static <T> ApiResponse<Page<T>> of(Page<T> page) {
        return of(page, MessageConstants.PAGINATION_SUCCESS);
    }
}
