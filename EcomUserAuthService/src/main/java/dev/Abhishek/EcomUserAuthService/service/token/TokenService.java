package dev.Abhishek.EcomUserAuthService.service.token;

import dev.Abhishek.EcomUserAuthService.entity.JwtObject;
import dev.Abhishek.EcomUserAuthService.entity.Token;
import dev.Abhishek.EcomUserAuthService.entity.User;
import dev.Abhishek.EcomUserAuthService.exceptions.JwtDeserializationException;
import dev.Abhishek.EcomUserAuthService.exceptions.JwtSerializationException;

public interface TokenService {
    public Token createToken(User user)throws JwtSerializationException;
    JwtObject validateToken(String token)throws JwtDeserializationException;
    JwtObject deserializeJwtObject(String tokenValue)throws JwtDeserializationException;
    String serializeJwtObject(JwtObject jwtObject)throws JwtSerializationException;
}
