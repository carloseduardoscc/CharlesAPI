package com.carlos.charles_api.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StandardError implements Serializable {
    private LocalDateTime timeStamp = LocalDateTime.now();
    private Integer status;
    private String error;
    private String message;
    private String path;

    public StandardError(Integer status, String error, String message, String path) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    @Override
    public String toString() {
        return "{" +
                "\n\t\"timeStamp\": " +"\"" + timeStamp + "\"" +
                ",\n\t\"status\": " + status +
                ",\n\t\"error\": " +"\"" + error + "\"" +
                ",\n\t\"message\": " +"\"" + message + "\"" +
                ",\n\t\"path\": " +"\"" + path + "\"" +
                "\n}";
    }
}
