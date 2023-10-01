package io.voitovich.testmodsentask.bookservice.exceptionhandler;


import io.voitovich.testmodsentask.bookservice.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestControllerExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(NotValidUUIDException.class)
    public ResponseEntity<Object> handleNotValidUUIDException(NotValidUUIDException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchRecordException.class)
    public ResponseEntity<Object> handleNoSuchRecordException(NoSuchRecordException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }
}
