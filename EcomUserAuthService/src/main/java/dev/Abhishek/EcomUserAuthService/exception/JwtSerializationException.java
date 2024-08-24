package dev.Abhishek.EcomUserAuthService.exception;

public class JwtSerializationException extends JwtException{
    public JwtSerializationException(String message) {
        super(message);
    }
}
