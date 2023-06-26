package com.cuping.cupingbe.global.exception;

import com.cuping.cupingbe.global.util.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // CustomException 클래스에서 발생하는 예외 핸들러
    @ExceptionHandler(value = {CustomException.class})
    public ResponseEntity<Message> handleCustomException(CustomException e) {
        Message exceptionMessage = Message.setSuccess(e.getErrorCode().getDetail(), null);
        log.error(e.getErrorCode().getDetail());
        return new ResponseEntity<>(exceptionMessage, e.getErrorCode().getHttpStatus());
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Message> handleException(Exception e){
        log.error("exception");
        log.error(e.getMessage());
        Message exceptionMessage = Message.setSuccess(e.getMessage(), e.getCause());
        return new ResponseEntity<>(exceptionMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<Message> handleRunTimeException(RuntimeException e) {
        log.error("run");
        log.error(e.getMessage());
        Message exceptionMessage = Message.setSuccess(e.getMessage(), e.getCause());
        return new ResponseEntity<>(exceptionMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {

        BindingResult bindingResult = ex.getBindingResult();

        StringBuilder builder = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            builder.append(fieldError.getDefaultMessage());
        }
        log.error(builder.toString());
        return new ResponseEntity<>(new Message(builder.toString(), null), HttpStatus.BAD_REQUEST);
    }
}