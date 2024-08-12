package dev.Abhishek.EcomUserAuthService.controller;

import dev.Abhishek.EcomUserAuthService.entity.JwtObject;
import dev.Abhishek.EcomUserAuthService.entity.dto.LoginRequestDto;
import dev.Abhishek.EcomUserAuthService.entity.dto.SignupRequestDto;
import dev.Abhishek.EcomUserAuthService.entity.dto.UserResponseDto;
import dev.Abhishek.EcomUserAuthService.service.user.UserService;
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
        return ResponseEntity.ok(userService.login(requestDto,response));
    }
    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> signup(@RequestBody SignupRequestDto requestDto){
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
