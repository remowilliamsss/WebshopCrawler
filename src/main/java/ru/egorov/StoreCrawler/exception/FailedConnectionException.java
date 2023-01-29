package ru.egorov.StoreCrawler.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FailedConnectionException extends RuntimeException {
    private String message;
    public static final String FAILED_CONNECTION = "Failed connection to %s.";

    public FailedConnectionException(String url) {
        message = String.format(FAILED_CONNECTION, url);
    }
}
