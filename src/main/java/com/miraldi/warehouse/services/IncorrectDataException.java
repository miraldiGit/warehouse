package com.miraldi.warehouse.services;

import lombok.Getter;

import java.util.List;

public class IncorrectDataException extends RuntimeException{
    @Getter
    private List<String> errors;
    @Getter
    private String error;

    public IncorrectDataException() {
        super();
    }

    public IncorrectDataException(List<String> messages) {
        super();
        this.errors = messages;
    }

    public IncorrectDataException(String message) {
        super(message);
        this.error = message;
    }
}
