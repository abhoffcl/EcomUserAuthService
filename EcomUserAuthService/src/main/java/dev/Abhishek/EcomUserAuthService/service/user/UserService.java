package dev.Abhishek.EcomUserAuthService.service.user;

import dev.Abhishek.EcomUserAuthService.entity.JwtObject;
import dev.Abhishek.EcomUserAuthService.dto.LoginRequestDto;
import dev.Abhishek.EcomUserAuthService.dto.SignupRequestDto;
import dev.Abhishek.EcomUserAuthService.dto.UserResponseDto;
import dev.Abhishek.EcomUserAuthService.exception.*;
import jakarta.servlet.http.HttpServletResponse;

public interface UserService {
    UserResponseDto signup(SignupRequestDto requestDto)throws RoleNotFoundException, KafkaMessagingException;
    UserResponseDto login(LoginRequestDto requestDto, HttpServletResponse response) throws
            JwtSerializationException,
            UserNotFoundException,
            InvalidCredentialsException;
    boolean logout(String token);
    JwtObject validate(String token);



}
