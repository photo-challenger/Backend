package com.photoChallenger.tripture.domain.controllerAdvice.dto;

import com.photoChallenger.tripture.global.exception.InputFieldException;
import org.springframework.web.bind.MethodArgumentNotValidException;

public class InputFieldErrorResponse extends ErrorResponse {
    String field;

    private InputFieldErrorResponse(final String message, final String field) {
        super(message);
        this.field = field;
    }

    public static InputFieldErrorResponse from(final InputFieldException exception) {
        return new InputFieldErrorResponse(exception.getMessage(), exception.getField());
    }

    public static InputFieldErrorResponse from(final MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        String field = exception.getFieldErrors().get(0).getField();
        return new InputFieldErrorResponse(message, field);
    }
}
