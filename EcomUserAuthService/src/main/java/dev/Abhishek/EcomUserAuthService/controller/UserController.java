package dev.Abhishek.EcomUserAuthService.controller;

import dev.Abhishek.EcomUserAuthService.entity.JwtObject;
import dev.Abhishek.EcomUserAuthService.dto.LoginRequestDto;
import dev.Abhishek.EcomUserAuthService.dto.SignupRequestDto;
import dev.Abhishek.EcomUserAuthService.dto.UserResponseDto;
import dev.Abhishek.EcomUserAuthService.service.user.UserService;
import dev.Abhishek.EcomUserAuthService.util.ValidationHelper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> login(@RequestBody LoginRequestDto requestDto, HttpServletResponse response){
        ValidationHelper.validateEmail(requestDto.getEmail());
        ValidationHelper.validatePassword(requestDto.getPassword());
        return ResponseEntity.ok(userService.login(requestDto,response));
    }
    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> signup(@RequestBody SignupRequestDto requestDto){
        ValidationHelper.validateName(requestDto.getName());
        ValidationHelper.validateEmail(requestDto.getEmail());
        ValidationHelper.validatePassword(requestDto.getPassword());
        ValidationHelper.validatePhoneNumber(requestDto.getPhoneNumber());
        return ResponseEntity.ok(userService.signup(requestDto));
    }
    @GetMapping("/logout")
    public ResponseEntity<Boolean> logout(@RequestHeader("Authorization")String token){
        return ResponseEntity.ok(userService.logout(token));
    }
    @GetMapping("/validate")
    public ResponseEntity<JwtObject> validate(@RequestParam("token") String token){
        return ResponseEntity.ok(userService.validate(token)) ;
    }

}
