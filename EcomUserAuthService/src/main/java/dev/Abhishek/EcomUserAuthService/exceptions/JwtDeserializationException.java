package dev.Abhishek.EcomUserAuthService.exceptions;

public class JwtDeserializationException extends JwtException{
    public JwtDeserializationException(String message) {
        super(message);
    }
}
