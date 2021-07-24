package com.scooterrental.scooter_rental.exception;

import io.jsonwebtoken.JwtException;
import org.hibernate.HibernateException;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.PersistenceException;
import java.sql.SQLException;


//@ControllerAdvice
@RestControllerAdvice
public class MyExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<?> unexpectedHandlerServiceException(ServiceException ex,
                                                                    WebRequest request) {
        ApiException apiException = new ApiException(ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> badRequest(BadRequestException exception, WebRequest request) {
        ApiException apiException = new ApiException(exception.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> unhandledException(Exception ex, WebRequest request) {
        ApiException apiException = new ApiException(ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(apiException, HttpStatus.INTERNAL_SERVER_ERROR);
    }


//    @ExceptionHandler
//    public ResponseEntity<IncorrectDataInput> unexpectedHandlerServiceException(Exception ex) {
//
//        IncorrectDataInput incorrectDataInput = new IncorrectDataInput();
//        incorrectDataInput.setInfo(ex.getMessage());
//        return new ResponseEntity<>(incorrectDataInput, HttpStatus.INTERNAL_SERVER_ERROR);
//    }

}