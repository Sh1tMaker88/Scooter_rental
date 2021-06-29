package com.scooterrental.scooter_rental.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class MyExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<Object> unexpectedHandlerServiceException(ServiceException ex,
                                                                    WebRequest request) {
        ApiException apiException = new ApiException(ex.getMessage(), ex.getCause());
        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> badRequest(BadRequestException exception) {
        ApiException apiException = new ApiException(exception.getMessage(), exception.getCause());
        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler
//    public ResponseEntity<IncorrectDataInput> unexpectedHandlerServiceException(Exception ex) {
//
//        IncorrectDataInput incorrectDataInput = new IncorrectDataInput();
//        incorrectDataInput.setInfo(ex.getMessage());
//        return new ResponseEntity<>(incorrectDataInput, HttpStatus.INTERNAL_SERVER_ERROR);
//    }

}