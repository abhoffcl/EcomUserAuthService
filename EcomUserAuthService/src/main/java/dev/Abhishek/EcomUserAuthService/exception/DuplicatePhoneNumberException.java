package dev.Abhishek.EcomUserAuthService.exception;

public class DuplicatePhoneNumberException extends RuntimeException{
    public DuplicatePhoneNumberException(String message) {
        super(message);
    }
}
