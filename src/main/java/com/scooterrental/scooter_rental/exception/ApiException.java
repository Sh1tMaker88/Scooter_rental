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
    private String details;
    private Throwable cause;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> errors;

    public ApiException(String message, String details) {
        this.whenExceptionHappen = LocalDateTime.now();
        this.message = message;
        this.details = details;
    }

    public ApiException(String message, String details, Throwable cause) {
        this.whenExceptionHappen = LocalDateTime.now();
        this.message = message;
        this.details = details;
        this.cause = cause;
    }
}
