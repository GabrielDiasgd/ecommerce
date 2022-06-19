package br.com.ecommerce.common.exceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ErrorResponse {

    private LocalDateTime timestamp;
    private Integer status;

    private List<Field> fields = new ArrayList<>();

    public ErrorResponse(LocalDateTime timestamp, Integer status, List<Field> fields) {
        this.timestamp = timestamp;
        this.status = status;
        this.fields = fields;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public Integer getStatus() {
        return status;
    }

    public List<Field> getFields() {
        return fields;
    }
}

