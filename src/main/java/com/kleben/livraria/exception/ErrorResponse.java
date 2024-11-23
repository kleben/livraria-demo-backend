package com.kleben.livraria.exception;

public class ErrorResponse {
    private int status;
    private String message;

    // Construtores
    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    // Getters e Setters
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
