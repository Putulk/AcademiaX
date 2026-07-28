package com.nextgen.erp.faculty_management.dto;

import lombok.Data;

@Data
public class ApiResponse<T> {

    private Boolean success;

    private String message;

    private T data;
}
