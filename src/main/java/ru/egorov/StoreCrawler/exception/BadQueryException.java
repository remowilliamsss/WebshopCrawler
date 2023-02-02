package ru.egorov.StoreCrawler.exception;

public class BadQueryException extends RuntimeException {

    public BadQueryException(String message) {
        super(message);
    }
}
