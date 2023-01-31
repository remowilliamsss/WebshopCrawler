package ru.egorov.StoreCrawler.controller.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.egorov.StoreCrawler.controller.ProductsController;
import ru.egorov.StoreCrawler.dto.ErrorDto;
import ru.egorov.StoreCrawler.exception.BadQueryException;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {
    public static final String HANDLED_EXCEPTION = "Handled the exception:";
    public static final String STORE_NOT_SUPPORTED = "this store is not supported";

    @ExceptionHandler
    public ResponseEntity<ErrorDto> handleException(BadQueryException e) {
        log.error(HANDLED_EXCEPTION, e);

        return new ResponseEntity<>(new ErrorDto(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorDto> handleException(MethodArgumentTypeMismatchException e) {
        log.error(HANDLED_EXCEPTION, e);

        String message = e.getMessage();
        String paramName = e.getName();

        if (paramName.equals(ProductsController.STORE)) {
            message = STORE_NOT_SUPPORTED;
        }

        return new ResponseEntity<>(new ErrorDto(message), HttpStatus.NOT_FOUND);
    }
}
