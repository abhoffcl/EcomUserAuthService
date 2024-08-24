package dev.Abhishek.EcomUserAuthService.exception;

public class JwtDeserializationException extends JwtException{
    public JwtDeserializationException(String message) {
        super(message);
    }
}
