package dev.Abhishek.EcomUserAuthService.service.role;

import dev.Abhishek.EcomUserAuthService.entity.Role;
import dev.Abhishek.EcomUserAuthService.dto.RoleRequestDto;
import dev.Abhishek.EcomUserAuthService.dto.RoleResponseDto;
import dev.Abhishek.EcomUserAuthService.exceptions.RoleNotFoundException;
import dev.Abhishek.EcomUserAuthService.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {
    private RoleRepository roleRepository;
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
    @Override
    public RoleResponseDto createRole(RoleRequestDto roleRequestDto) {
        Role role = convertRoleRequestDtoToRole(roleRequestDto);
        return convertRoleEntityToRoleResponseDto(roleRepository.save(role));
    }
    @Override
    public Boolean deleteRole(UUID id) {
        roleRepository.deleteById(id);
        return true;
    }
    @Override
    public RoleResponseDto getRole(UUID id) {
        Role role= roleRepository.findById(id).
                orElseThrow(()->new RoleNotFoundException("Role not found for id :"+id));
        return convertRoleEntityToRoleResponseDto(role);
    }
    @Override
    public RoleResponseDto updateRole(RoleRequestDto roleRequestDto,UUID id) {
        Role savedRole = roleRepository.findById(id).
                orElseThrow(()->new RoleNotFoundException("Role not found for id :"+id));
        savedRole.setRoleName(roleRequestDto.getRoleName());
        savedRole.setDescription(roleRequestDto.getDescription());
        Role updatedRole = roleRepository.save(savedRole);
        return convertRoleEntityToRoleResponseDto(updatedRole);
    }
    @Override
    public List<RoleResponseDto> getAllRoles() {
        List<Role>savedRoles=roleRepository.findAll();
        List<RoleResponseDto>roleResponseDtos =!(savedRoles==null || savedRoles.isEmpty())?savedRoles.
                stream().
                map(savedRole->convertRoleEntityToRoleResponseDto(savedRole)).
                collect(Collectors.toList()) :  new ArrayList<>();
        return roleResponseDtos ;
    }
    public  RoleResponseDto convertRoleEntityToRoleResponseDto(Role role){
        RoleResponseDto roleResponseDto =new RoleResponseDto();
        roleResponseDto.setId(role.getId());
        roleResponseDto.setRoleName(role.getRoleName());
        roleResponseDto.setDescription(role.getDescription());
        return roleResponseDto;
    }
    public  Role convertRoleRequestDtoToRole(RoleRequestDto roleRequestDto){
        Role role = new Role();
        role.setDescription(roleRequestDto.getDescription());
        role.setRoleName(roleRequestDto.getRoleName());
        return role;
    }

}
