package ru.egorov.StoreCrawler.http;

// TODO: 20.11.2022 И request, и response - это лишь DTO.
//  Непонятно, зачем велосипед, если ты знаком с ResponseEntity
public class ErrorResponse {
    private String message;
    private long timestamp;

    public ErrorResponse(String message, long timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
