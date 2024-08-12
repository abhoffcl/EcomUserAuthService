package dev.Abhishek.EcomUserAuthService.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.Abhishek.EcomUserAuthService.controller.UserController;
import dev.Abhishek.EcomUserAuthService.entity.dto.ExceptionResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackageClasses = UserController.class)
public class UserControllerExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity handleUserNotFoundException(UserNotFoundException ue){
        ExceptionResponseDto exceptionResponseDto = new ExceptionResponseDto(
                ue.getMessage(),
                404
        );
        return new ResponseEntity<>(exceptionResponseDto, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity handleInvalidCredentialsException(InvalidCredentialsException ie){
        ExceptionResponseDto exceptionResponseDto = new ExceptionResponseDto(
                ie.getMessage(),
                401
        );
        return new ResponseEntity<>(exceptionResponseDto, HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity handleJsonProcessingException(JsonProcessingException je){
        ExceptionResponseDto exceptionResponseDto = new ExceptionResponseDto(
                je.getMessage(),
                401
        );
        return new ResponseEntity<>(exceptionResponseDto, HttpStatus.UNAUTHORIZED);
    }
}
