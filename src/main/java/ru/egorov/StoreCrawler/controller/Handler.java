package ru.egorov.StoreCrawler.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.egorov.StoreCrawler.dto.Response;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class Handler {
    private static final Logger log = LoggerFactory.getLogger(Handler.class);

    @ExceptionHandler
    public ResponseEntity<Response> handleException(ConstraintViolationException e) {
        String message = e.getMessage()
                .substring(e.getMessage()
                        .indexOf(" ") + 1);

        log.error("Handled the exception with the message: \"{}\"", message);

        return new ResponseEntity<>(new Response(message), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<Response> handleException(MethodArgumentNotValidException e) {
        String message = e.getFieldError().getDefaultMessage();

        log.error("Handled the exception with the message: \"{}\"", message);

        return new ResponseEntity<>(new Response(message), HttpStatus.BAD_REQUEST);
    }
}
