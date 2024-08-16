package dev.Abhishek.EcomUserAuthService.controller;

import dev.Abhishek.EcomUserAuthService.dto.RoleRequestDto;
import dev.Abhishek.EcomUserAuthService.dto.RoleResponseDto;
import dev.Abhishek.EcomUserAuthService.exceptions.InvalidInputException;
import dev.Abhishek.EcomUserAuthService.service.role.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/role")
public class RoleController {

    private RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/create")
    ResponseEntity<RoleResponseDto> createRole(@RequestBody RoleRequestDto requestDto){
        return ResponseEntity.ok(roleService.createRole(requestDto));
    }
    @GetMapping("/delete")
    ResponseEntity<Boolean>deleteRole(@PathVariable("id")UUID id){
        return ResponseEntity.ok(roleService.deleteRole(id));
    }
    @GetMapping("/{id}")
    ResponseEntity<RoleResponseDto>getRole(@PathVariable("id")UUID id){
        if(id==null)
            throw new InvalidInputException("Input is not correct");
        return ResponseEntity.ok( roleService.getRole(id));
    }
    @GetMapping()
    ResponseEntity<List<RoleResponseDto>>getALLRoles(){
        List<RoleResponseDto> roles = roleService.getAllRoles();
        return ResponseEntity.ok(roles);
    }


}
