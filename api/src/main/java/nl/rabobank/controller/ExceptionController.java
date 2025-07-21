package nl.rabobank.controller;

import nl.rabobank.exceptions.InvalidAccountException;
import nl.rabobank.model.ErrorResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@AllArgsConstructor
@Slf4j
public class ExceptionController {


    @ExceptionHandler(InvalidAccountException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleException(InvalidAccountException e) {
        log.warn(e.getMessage());
        return new ResponseEntity<>(createErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error(e.getMessage(),e);
        return new ResponseEntity<>(createErrorResponse("Something went wrong, please contact support"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ErrorResponse createErrorResponse(String errorMessage) {
        return new ErrorResponse(errorMessage);
    }
}


