package dev.Abhishek.EcomUserAuthService.exception;

public class KafkaMessagingException extends RuntimeException{
    public KafkaMessagingException(String message) {
        super(message);
    }
}
