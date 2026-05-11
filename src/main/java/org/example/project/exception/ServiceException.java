package org.example.project.exception;

import lombok.Getter;

@Getter
public class ServiceException extends RuntimeException {
    private final String field;

    public ServiceException(String field, String message) {
        super(message);
        this.field = field;
    }

}
