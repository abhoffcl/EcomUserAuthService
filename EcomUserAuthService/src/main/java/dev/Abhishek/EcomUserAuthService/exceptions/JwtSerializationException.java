package dev.Abhishek.EcomUserAuthService.exceptions;

public class JwtSerializationException extends JwtException{
    public JwtSerializationException(String message) {
        super(message);
    }
}
