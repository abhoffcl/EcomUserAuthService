package dev.Abhishek.EcomUserAuthService.service.role;

import dev.Abhishek.EcomUserAuthService.dto.RoleRequestDto;
import dev.Abhishek.EcomUserAuthService.dto.RoleResponseDto;

import java.util.List;
import java.util.UUID;

public interface RoleService {
    RoleResponseDto createRole(RoleRequestDto roleRequestDto);
    List<RoleResponseDto>getAllRoles();
    RoleResponseDto getRole(UUID id);
    RoleResponseDto updateRole(RoleRequestDto roleRequestDto,UUID id);
    Boolean deleteRole(UUID id);





}
