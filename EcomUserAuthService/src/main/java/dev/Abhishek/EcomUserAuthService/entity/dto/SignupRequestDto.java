package dev.Abhishek.EcomUserAuthService.entity.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class SignupRequestDto {
    private String name;
    private String email;
    private String password;
    private String phoneNumber;
    private List<UUID> roleIds;
}
