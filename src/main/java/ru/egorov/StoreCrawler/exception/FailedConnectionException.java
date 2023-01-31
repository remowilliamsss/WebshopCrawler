package ru.egorov.StoreCrawler.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FailedConnectionException extends RuntimeException {
    private String message;

    public FailedConnectionException(String url) {
        message = String.format("Failed connection to %s.", url);
    }
}
