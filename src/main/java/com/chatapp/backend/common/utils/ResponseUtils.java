package com.chatapp.backend.common.utils;

import com.chatapp.backend.common.constants.MessageConstants;
import com.chatapp.backend.common.dto.ApiResponse;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Utility class for creating common API responses and pagination
 */
public class ResponseUtils {
    
    /**
     * Create a paginated response with content as List and pagination info
     * @param pageResult Spring Data Page object
     * @param page Current page number (1-based)
     * @param size Page size
     * @param message Success message
     * @return ApiResponse with List data and pagination info
     */
    public static <T> ApiResponse<List<T>> createPaginatedResponse(Page<T> pageResult, int page, int size, String message) {
        return ApiResponse.<List<T>>builder()
                .success(true)
                .code(200)
                .message(message)
                .data(pageResult.getContent())
                .pagination(createPaginationInfo(pageResult, page, size))
                .build();
    }
    
    /**
     * Create a paginated response with default success message
     */
    public static <T> ApiResponse<List<T>> createPaginatedResponse(Page<T> pageResult, int page, int size) {
        return createPaginatedResponse(pageResult, page, size, MessageConstants.PAGINATION_SUCCESS);
    }
    
    /**
     * Create a paginated response with custom message using default pagination
     */
    public static <T> ApiResponse<List<T>> createPaginatedResponse(Page<T> pageResult, String message) {
        return createPaginatedResponse(pageResult, pageResult.getNumber() + 1, pageResult.getSize(), message);
    }
    
    /**
     * Create pagination info from Spring Data Page
     * @param page Spring Data Page object
     * @param currentPage Current page number (1-based)
     * @param pageSize Page size
     * @return PaginationInfo object
     */
    public static <T> ApiResponse.PaginationInfo createPaginationInfo(Page<T> page, int currentPage, int pageSize) {
        return ApiResponse.PaginationInfo.builder()
                .page(currentPage)
                .limit(pageSize)
                .total(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .build();
    }
    
    /**
     * Create pagination info from Spring Data Page using page's own pagination
     */
    public static <T> ApiResponse.PaginationInfo createPaginationInfo(Page<T> page) {
        return createPaginationInfo(page, page.getNumber() + 1, page.getSize());
    }
    
    /**
     * Create pagination info manually
     * @param page Current page number (1-based)
     * @param limit Page size
     * @param total Total number of items
     * @return PaginationInfo object
     */
    public static ApiResponse.PaginationInfo createPaginationInfo(int page, int limit, long total) {
        int totalPages = (int) Math.ceil((double) total / limit);
        return ApiResponse.PaginationInfo.builder()
                .page(page)
                .limit(limit)
                .total(total)
                .totalPages(totalPages)
                .hasNext(page < totalPages)
                .hasPrevious(page > 1)
                .build();
    }
}
