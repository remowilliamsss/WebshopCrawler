package ru.egorov.storecrawler.exception;

import lombok.Getter;

@Getter
public class FailedConnectionException extends RuntimeException {
    private final String message;

    public FailedConnectionException(String url) {
        message = String.format("Failed connection to %s.", url);
    }
}
