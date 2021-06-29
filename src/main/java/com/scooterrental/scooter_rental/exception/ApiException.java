package com.scooterrental.scooter_rental.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiException {
    private LocalDateTime whenExceptionHappen;
    private String message;
    private String debugMessage;
    private Throwable cause;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> errors;

    public ApiException(String message, String debugMessage) {
        this.whenExceptionHappen = LocalDateTime.now();
        this.message = message;
        this.debugMessage = debugMessage;
    }

    public ApiException(String message, Throwable cause) {
        this.whenExceptionHappen = LocalDateTime.now();
        this.message = message;
        this.cause = cause;
    }
}
