package dev.Abhishek.EcomUserAuthService.exceptions;

public class JwtException extends RuntimeException{
    public JwtException(String message) {
        super(message);
    }
}
