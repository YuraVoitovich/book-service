package io.voitovich.testmodsentask.bookservice.exception;

public class KafkaSendingException extends RuntimeException {
    public KafkaSendingException() {
        super();
    }

    public KafkaSendingException(String message) {
        super(message);
    }

    public KafkaSendingException(String message, Throwable cause) {
        super(message, cause);
    }
}
