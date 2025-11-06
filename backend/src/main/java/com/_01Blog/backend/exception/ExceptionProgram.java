package com._01Blog.backend.exception;

public class ExceptionProgram extends Exception {
    private int status;
    private String message;

    public ExceptionProgram(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return this.status;
    }

    public String getMessage() {
        return this.message;
    }
}
