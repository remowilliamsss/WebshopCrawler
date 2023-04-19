package ru.egorov.storecrawler.controller.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.egorov.storecrawler.dto.ErrorDto;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {
    public static final String STORE_NOT_SUPPORTED = "this store is not supported";

    @ExceptionHandler
    public ResponseEntity<ErrorDto> handleException(MethodArgumentTypeMismatchException e) {
        log.error("Handled the exception:", e);

        String message = e.getMessage();
        String paramName = e.getName();

        if (paramName.equals("store")) {
            message = STORE_NOT_SUPPORTED;
        }

        return new ResponseEntity<>(new ErrorDto(message), HttpStatus.NOT_FOUND);
    }
}
