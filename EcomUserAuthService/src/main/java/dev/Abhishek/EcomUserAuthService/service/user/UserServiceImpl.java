package dev.Abhishek.EcomUserAuthService.service.user;

import dev.Abhishek.EcomUserAuthService.entity.JwtObject;
import dev.Abhishek.EcomUserAuthService.entity.Role;
import dev.Abhishek.EcomUserAuthService.entity.Token;
import dev.Abhishek.EcomUserAuthService.entity.User;
import dev.Abhishek.EcomUserAuthService.dto.LoginRequestDto;
import dev.Abhishek.EcomUserAuthService.dto.RoleResponseDto;
import dev.Abhishek.EcomUserAuthService.dto.SignupRequestDto;
import dev.Abhishek.EcomUserAuthService.dto.UserResponseDto;
import dev.Abhishek.EcomUserAuthService.exception.*;
import dev.Abhishek.EcomUserAuthService.repository.RoleRepository;
import dev.Abhishek.EcomUserAuthService.repository.TokenRepository;
import dev.Abhishek.EcomUserAuthService.repository.UserRepository;
import dev.Abhishek.EcomUserAuthService.service.kafkaProducer.KafkaMessagingService;
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
    private KafkaMessagingService kafkaMessagingService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, RoleService roleService, TokenRepository tokenRepository, TokenService tokenService, KafkaMessagingService kafkaMessagingService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.roleService = roleService;
        this.tokenRepository = tokenRepository;
        this.tokenService = tokenService;
        this.kafkaMessagingService = kafkaMessagingService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserResponseDto signup(SignupRequestDto requestDto)throws RoleNotFoundException ,KafkaMessagingException{
        List<Role>roles = new ArrayList<>();
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new DuplicateEmailException("Email address already in use.");
        }
        if (userRepository.findByPhoneNumber(requestDto.getPhoneNumber()).isPresent()) {
            throw new DuplicatePhoneNumberException("Phone number already in use.");
        }
        for(UUID roleId:requestDto.getRoleIds()){
            Role role = roleRepository.findById(roleId).
                    orElseThrow(()->new RoleNotFoundException("Role not available with id "+roleId));
            roles.add(role);
        }


        User user = new User();
        user.setName(requestDto.getName());
        user.setPhoneNumber(requestDto.getPhoneNumber());
        user.setPassword(bCryptPasswordEncoder.encode(requestDto.getPassword()));
        user.setEmail(requestDto.getEmail());
        user.setRoles(roles);
        User savedUser = userRepository.save(user);
        kafkaMessagingService.send(requestDto);
        return convertUserEntityToUserResponseDto(savedUser);
    }
    @Override
    public UserResponseDto login(LoginRequestDto requestDto, HttpServletResponse response) throws
            JwtSerializationException,
            UserNotFoundException,
            InvalidCredentialsException {
        String userEmail=requestDto.getEmail();
        User savedUser = userRepository.findByEmail(userEmail).
                orElseThrow(()->new UserNotFoundException("User not found with email "+userEmail));
        if(bCryptPasswordEncoder.matches(requestDto.getPassword(),savedUser.getPassword())){
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
