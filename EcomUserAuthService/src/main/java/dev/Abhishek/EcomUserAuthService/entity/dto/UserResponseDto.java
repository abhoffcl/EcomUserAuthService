package dev.Abhishek.EcomUserAuthService.entity.dto;

import dev.Abhishek.EcomUserAuthService.entity.Role;
import dev.Abhishek.EcomUserAuthService.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class UserResponseDto {
    private UUID userId;
    private String name;
    private String email;
    private String phoneNumber;
    private List<RoleResponseDto> roles;


}
