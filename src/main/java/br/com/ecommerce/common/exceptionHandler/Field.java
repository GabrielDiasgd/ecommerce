package br.com.ecommerce.common.exceptionHandler;

public class Field {
        private String field;
        private String message;
        public Field(String field, String message) {
            this.field = field;
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public String getField() {
            return field;
        }
}
