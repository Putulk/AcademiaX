package com.nextgen.erp.auth.entity;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@Builder
public class ApiError{
    private String status;
    private String message;
    private LocalDateTime timeStamp;
}
