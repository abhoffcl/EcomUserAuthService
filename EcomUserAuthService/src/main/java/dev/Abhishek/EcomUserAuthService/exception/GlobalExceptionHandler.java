package dev.Abhishek.EcomUserAuthService.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.Abhishek.EcomUserAuthService.dto.ExceptionResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity handleUserNotFoundException(UserNotFoundException ue){
        ExceptionResponseDto exceptionResponseDto = new ExceptionResponseDto(
                ue.getMessage(),
                404
        );
        return new ResponseEntity<>(exceptionResponseDto, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity handleInvalidInputException(InvalidInputException ie){
        ExceptionResponseDto exceptionResponseDto = new ExceptionResponseDto(
                ie.getMessage(),
                401
        );
        return new ResponseEntity<>(exceptionResponseDto, HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity handleInvalidCredentialsException(InvalidCredentialsException ie){
        ExceptionResponseDto exceptionResponseDto = new ExceptionResponseDto(
                ie.getMessage(),
                401
        );
        return new ResponseEntity<>(exceptionResponseDto, HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(JwtException.class)
    public ResponseEntity handleJwtException(JwtException je){
        ExceptionResponseDto exceptionResponseDto = new ExceptionResponseDto(
                je.getMessage(),
                401
        );
        return new ResponseEntity<>(exceptionResponseDto, HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(KafkaMessagingException.class)
    public ResponseEntity handleKafkaMessagingException(KafkaMessagingException ke){
        ExceptionResponseDto exceptionResponseDto = new ExceptionResponseDto(
                ke.getMessage(),
                500
        );
        return new ResponseEntity<>(exceptionResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
