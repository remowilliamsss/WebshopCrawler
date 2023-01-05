package ru.egorov.StoreCrawler.controller.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.egorov.StoreCrawler.dto.ErrorResponse;
import ru.egorov.StoreCrawler.exception.BadQueryException;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(BadQueryException e) {
        String message = e.getMessage();

        log.error("Handled the exception with the message: \"{}\"", message);

        return new ResponseEntity<>(new ErrorResponse(message), HttpStatus.BAD_REQUEST);
    }
}
