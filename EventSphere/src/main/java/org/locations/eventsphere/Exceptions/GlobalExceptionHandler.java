package org.locations.eventsphere.Exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity handleUserAlreadyExistsException(UserAlreadyExistsException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        Map<String,String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName,errorMessage);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
    @ExceptionHandler(EventAlreadyExistsException.class)
    public ResponseEntity handleEventAlreadyExistsException(EventAlreadyExistsException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
    @ExceptionHandler(NoSuchCategoryException.class)
    public ResponseEntity handleNoSuchCategoryException(NoSuchCategoryException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
    @ExceptionHandler(NoSuchEventException.class)
    public ResponseEntity handleNoSuchEventException(NoSuchEventException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
    @ExceptionHandler(NoSuchRoleException.class)
    public ResponseEntity handleNoSuchRoleException(NoSuchRoleException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
    @ExceptionHandler(NoSuchUserException.class)
    public ResponseEntity handleNoSuchUserException(NoSuchUserException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
    @ExceptionHandler(NoSuchTokenException.class)
    public ResponseEntity handleNoSuchTokenException(NoSuchTokenException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
    @ExceptionHandler(IOException.class)
    public  ResponseEntity handleIOException(IOException e){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong, try again later");
    }
    @ExceptionHandler(eventImageProcessingException.class)
    public ResponseEntity handleEventImageProcessingException(eventImageProcessingException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
    @ExceptionHandler(EventImageAlreadyExists.class)
    public ResponseEntity handleEventImageAlreadyExists(EventImageAlreadyExists e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
    @ExceptionHandler(subscribeProcessingException.class)
    public ResponseEntity handleSubscribeProcessingException(subscribeProcessingException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
    @ExceptionHandler(NoSuchPoolException.class)
    public ResponseEntity handleNoSuchPoolException(NoSuchPoolException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
    @ExceptionHandler(PoolException.class)
    public ResponseEntity handlePoolException(PoolException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
    @ExceptionHandler(EventSphereException.class)
    public ResponseEntity handleEventSphereException(EventSphereException e){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
    @ExceptionHandler(EventException.class)
    public ResponseEntity handleEventException(EventException e){
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
    }
}
