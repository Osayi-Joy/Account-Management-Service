package com.interswitchgroup.accountmanagementsystem.common.utils.response;

import java.util.ArrayList;
import java.util.List;
import lombok.*;
/**
 * @author Joy Osayi
 * @createdOn April-12(Fri)-2024
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ApiResponseJson<T> {
    private String message;
    private boolean success;
    private T data;
    private List<ApiError> errors = new ArrayList<>();

    public void addError(ApiError error){
        errors.add(error);
    }

    public void addError(String errorMessage){
        errors.add(new ApiError(errorMessage));
    }
}
