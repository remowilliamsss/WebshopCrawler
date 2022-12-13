package ru.egorov.StoreCrawler.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.egorov.StoreCrawler.dto.ErrorResponse;

import javax.validation.ConstraintViolationException;

// TODO: 13.12.2022 вынеси в подпакет. И назвать лучше более развернуто. Аля ControllerExceptionHandler
@RestControllerAdvice
public class Handler {
    private static final Logger log = LoggerFactory.getLogger(Handler.class);

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(ConstraintViolationException e) {
        String message = e.getMessage()
                .substring(e.getMessage()
                        .indexOf(" ") + 1);

        log.error("Handled the exception with the message: \"{}\"", message);

        return new ResponseEntity<>(new ErrorResponse(message), HttpStatus.NOT_FOUND);
    }

    // TODO: 13.12.2022 каким образом ты планируешь допускать чект эксепшн в контроллере?
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException e) {
        String message = e.getFieldError().getDefaultMessage();

        log.error("Handled the exception with the message: \"{}\"", message);

        return new ResponseEntity<>(new ErrorResponse(message), HttpStatus.BAD_REQUEST);
    }
}
