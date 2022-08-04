package br.com.ecommerce.common.exceptionHandler;

import org.springframework.validation.FieldError;

public class Field {
        private String field;
        private String message;
        public Field(String field, String message) {
            this.field = field;
            this.message = message;
        }
    public Field(FieldError fieldError) {
        this.field = fieldError.getField();
        this.message = String.format("The field %s %s", fieldError.getField(), fieldError.getDefaultMessage());
    }

        public String getMessage() {
            return message;
        }

        public String getField() {
            return field;
        }
}
