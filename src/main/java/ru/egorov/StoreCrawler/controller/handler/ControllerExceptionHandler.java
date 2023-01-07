package ru.egorov.StoreCrawler.controller.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.egorov.StoreCrawler.dto.ErrorDto;
import ru.egorov.StoreCrawler.exception.BadQueryException;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorDto> handleException(BadQueryException e) {
        String message = e.getMessage();

        log.error("Handled the exception with the message: \"{}\"", message);

        return new ResponseEntity<>(new ErrorDto(message), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorDto> handleException(MethodArgumentTypeMismatchException e) {
        String message = e.getMessage();
        String paramName = e.getName();

        if (paramName.equals("store")) {
            message = "this store is not supported";
        }

        log.error("Handled the exception with the message: \"{}\"", message);

        return new ResponseEntity<>(new ErrorDto(message), HttpStatus.NOT_FOUND);
    }
}
