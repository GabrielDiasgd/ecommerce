package br.com.ecommerce.common.exceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ErrorResponse {

    private LocalDateTime timestamp;
    private Integer status;

    private String path;

    private String message;

    private List<Field> fields = new ArrayList<>();

    public ErrorResponse(LocalDateTime timestamp, Integer status, String path, List<Field> fields) {
        this.timestamp = timestamp;
        this.status = status;
        this.fields = fields;
        this.path = path;
    }

    public ErrorResponse(LocalDateTime timestamp, Integer status, String path, String message) {
        this.timestamp = timestamp;
        this.status = status;
        this.path = path;
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public Integer getStatus() {
        return status;
    }

    public String getPath() {
        return path;
    }

    public String getMessage() {
        return message;
    }

    public List<Field> getFields() {
        return fields;
    }
}

