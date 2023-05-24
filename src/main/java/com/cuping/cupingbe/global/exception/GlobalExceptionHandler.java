package com.cuping.cupingbe.global.exception;

import com.cuping.cupingbe.global.util.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        log.error(e.getMessage());
        Message exceptionMessage = Message.setSuccess(e.getMessage(), e.getCause());
        return new ResponseEntity<>(exceptionMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}