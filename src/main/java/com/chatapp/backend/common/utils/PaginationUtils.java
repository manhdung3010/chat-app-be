package com.chatapp.backend.common.utils;

import com.chatapp.backend.common.dto.ApiResponse;
import org.springframework.data.domain.Page;

public class PaginationUtils {
    
    public static <T> ApiResponse.PaginationInfo createPaginationInfo(Page<T> page) {
        return ApiResponse.PaginationInfo.builder()
                .page(page.getNumber() + 1) // Spring Data uses 0-based indexing
                .limit(page.getSize())
                .total(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .build();
    }
    
    public static <T> ApiResponse.PaginationInfo createPaginationInfo(int page, int limit, long total) {
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
