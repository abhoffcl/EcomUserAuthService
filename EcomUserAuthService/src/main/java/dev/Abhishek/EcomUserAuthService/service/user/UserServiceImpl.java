package dev.Abhishek.EcomUserAuthService.service.user;

import dev.Abhishek.EcomUserAuthService.entity.JwtObject;
import dev.Abhishek.EcomUserAuthService.entity.Role;
import dev.Abhishek.EcomUserAuthService.entity.Token;
import dev.Abhishek.EcomUserAuthService.entity.User;
import dev.Abhishek.EcomUserAuthService.entity.dto.LoginRequestDto;
import dev.Abhishek.EcomUserAuthService.entity.dto.RoleResponseDto;
import dev.Abhishek.EcomUserAuthService.entity.dto.SignupRequestDto;
import dev.Abhishek.EcomUserAuthService.entity.dto.UserResponseDto;
import dev.Abhishek.EcomUserAuthService.exceptions.InvalidCredentialsException;
import dev.Abhishek.EcomUserAuthService.exceptions.JwtSerializationException;
import dev.Abhishek.EcomUserAuthService.exceptions.RoleNotFoundException;
import dev.Abhishek.EcomUserAuthService.exceptions.UserNotFoundException;
import dev.Abhishek.EcomUserAuthService.repository.RoleRepository;
import dev.Abhishek.EcomUserAuthService.repository.TokenRepository;
import dev.Abhishek.EcomUserAuthService.repository.UserRepository;
import dev.Abhishek.EcomUserAuthService.service.role.RoleService;
import dev.Abhishek.EcomUserAuthService.service.role.RoleServiceImpl;
import dev.Abhishek.EcomUserAuthService.service.token.TokenService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private RoleService roleService;
    private TokenRepository tokenRepository;
    private TokenService tokenService;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, RoleService roleService, TokenRepository tokenRepository, TokenService tokenService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.roleService = roleService;
        this.tokenRepository = tokenRepository;
        this.tokenService = tokenService;
    }

    @Override
    public UserResponseDto signup(SignupRequestDto requestDto)throws RoleNotFoundException {
        List<Role>roles = new ArrayList<>();
        for(UUID roleId:requestDto.getRoleIds()){
            Role role = roleRepository.findById(roleId).
                    orElseThrow(()->new RoleNotFoundException("Role not available with id "+roleId));
            roles.add(role);
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User user = new User();
        user.setName(requestDto.getName());
        user.setPhoneNumber(requestDto.getPhoneNumber());
        user.setPassword(encoder.encode(requestDto.getPassword()));
        user.setEmail(requestDto.getEmail());
        user.setRoles(roles);
        return convertUserEntityToUserResponseDto(userRepository.save(user));
    }
    @Override
    public UserResponseDto login(LoginRequestDto requestDto, HttpServletResponse response) throws
            JwtSerializationException,
            UserNotFoundException,
            InvalidCredentialsException {
        User savedUser = userRepository.findByEmail(requestDto.getEmail()).
                orElseThrow(()->new UserNotFoundException("User not found"));
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if(encoder.matches(requestDto.getPassword(),savedUser.getPassword())){
            Token token =tokenService.createToken(savedUser);
            response.setHeader("Authorization", "Bearer " + token.getValue());
        }
        else {
            throw new InvalidCredentialsException("Invalid credentials");
        }
        return convertUserEntityToUserResponseDto(userRepository.save(savedUser));
    }
    @Override
    public boolean logout(String token) {
        Optional<Token> tokenOptional = tokenRepository.findByValueAndDeletedEquals(token, false);

        if (tokenOptional.isEmpty()) {
            new InvalidCredentialsException("Token is not valid");
        }
        Token tokenObject = tokenOptional.get();
        tokenObject.setDeleted(true);
        tokenRepository.save(tokenObject);
        return true;
    }

    @Override
    public JwtObject validate(String token) {
        return tokenService.validateToken(token);
    }

    public  UserResponseDto convertUserEntityToUserResponseDto(User user){
        if(user==null) return null;
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setName(user.getName());
        userResponseDto.setUserId(user.getId());
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setPhoneNumber(user.getPhoneNumber());
        List<Role>roles = user.getRoles();
        List<RoleResponseDto>roleResponseDtos= !(roles==null || roles.isEmpty())?roles.
                stream().
                map(role->((RoleServiceImpl)roleService).convertRoleEntityToRoleResponseDto(role)).
                collect(Collectors.toList()):new ArrayList<>();
        userResponseDto.setRoles(roleResponseDtos);
        userResponseDto.setPhoneNumber(user.getPhoneNumber());
        return userResponseDto;
    }
}
