package com.carlos.charles_api.controller.exceptionHandler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StandardError implements Serializable {
    private LocalDateTime timeStamp;
    private Integer status;
    private String error;
    private String message;
    private String path;
}
