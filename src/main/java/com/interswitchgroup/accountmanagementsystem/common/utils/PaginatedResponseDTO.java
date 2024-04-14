package com.interswitchgroup.accountmanagementsystem.common.utils;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Joy Osayi
 * @createdOn Apr-14(Sun)-2024
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PaginatedResponseDTO<T> {
    private List<T> content = new ArrayList<>();
    private int currentPage;
    private long totalPages;
    private long totalItems;
    private Boolean isFirstPage;
    private Boolean isLastPage;
}
