package com.bank.statement.dto;

import org.springframework.stereotype.Component;

@Component
public class Response {
    private String message;
    private boolean value;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }
}
