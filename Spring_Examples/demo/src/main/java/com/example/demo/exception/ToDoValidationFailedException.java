package com.example.demo.exception;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception is thrown if content for the to do entity is not transmitted.
 */
@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class TodoValidationFailedException extends RuntimeException {
    public TodoValidationFailedException() {
        super("Validation failed.");
    }
}
